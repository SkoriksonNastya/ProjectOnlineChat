package server;

import logger.Logger;
import configuration.Config;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private static final Logger LOGGER = Logger.getLog();

    private ServerSocket serverSocket;
    private Socket socket;

    private final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        Config config = Config.getInstance();
        Server serverChat = new Server();
        serverChat.listen(config.getPort());
    }

    public void listen(int port) {
        try {
            serverSocket = new ServerSocket(port);
            LOGGER.log("Сервер запущен!");
            System.out.println("Сервер запущен!");

            while (true) {
                socket = serverSocket.accept();
                final Connection connection = new Connection(socket);
                connections.add(connection);
                connection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    private void closeAll() {
        try {
            serverSocket.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Connection extends Thread {

        private BufferedReader in;
        private PrintWriter out;

        public Connection(Socket socket) {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String name = in.readLine();
                LOGGER.log("Пользователь " + name + " : онлайн");
                sendMessageAllConnection("Пользователь " + name + " : онлайн");

                String message;
                while (true) {
                    message = in.readLine();
                    if ("exit".equals(message)) {
                        break;
                    }
                    LOGGER.log(name + ": " + message);
                    sendMessageAllConnection(name + ": " + message);
                }

                LOGGER.log("Пользователь " + name + " : вышел из чата");
                sendMessageAllConnection("Пользователь " + name + " : вышел из чата");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeAll();
            }
        }

        private void sendMessage(String message) {
            out.println(message);
        }

        private void sendMessageAllConnection(String message) {
            synchronized(connections) {
                connections.forEach(connection -> connection.sendMessage(message));
            }
        }
        private void closeAll() {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}