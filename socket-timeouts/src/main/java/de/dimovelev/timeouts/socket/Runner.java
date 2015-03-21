package de.dimovelev.timeouts.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

public class Runner {
    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("Usage: java -jar socket-timeouts*.jar <host> <port> <connect-timeout> <so-timeout>");
            System.exit(1);
        }
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        final int connectTimeout = Integer.parseInt(args[2]);
        final int soTimeout = Integer.parseInt(args[3]);
        final Socket socket = new Socket();
        if (soTimeout > 0) {
            socket.setSoTimeout(soTimeout);
        }
        if (connectTimeout > 0) {
            socket.connect(new InetSocketAddress(host, port), connectTimeout);
        } else {
            socket.connect(new InetSocketAddress(host, port));
        }
        final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write("Hello\n");
        out.flush();
        final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(in.readLine());
        socket.close();
    }
}
