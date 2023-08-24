package com.github.jccode.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8081);
        client.echo("hello, world");
    }

    static class Client {
        private String host;
        private int port;

        public Client(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public void echo(String content) throws IOException {
            Socket client = new Socket(host, port);
            BufferedOutputStream outputStream = new BufferedOutputStream(client.getOutputStream());
            outputStream.write(content.getBytes("UTF-8"));
            outputStream.close();
            client.close();
        }
    }
}
