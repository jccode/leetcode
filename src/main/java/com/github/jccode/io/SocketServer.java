package com.github.jccode.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class SocketServer {

    public static void main(String[] args) throws IOException {
        new Server(8081).start();
    }

    static class Server {
        private int port;
        public Server(int port) {
            this.port = port;
        }
        public void start() throws IOException {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), port));
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
                    BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                    byte[] buffer = new byte[1024];
                    int totalLength = 0;
                    int len = 0;
                    while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                        totalLength += len;
                    }
                    System.out.printf("Receive from %s:%d total %d bytes, contents:%s%n",
                            remoteAddress.getAddress().getHostAddress(),
                            remoteAddress.getPort(),
                            totalLength,
                            new String(buffer));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

