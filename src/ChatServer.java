import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	private static final int PORT = 5000;
	private static final List<String> responses = List.of("Interessante... continue.", "Essa é uma boa pergunta.",
			"Você sempre pensa assim?");

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Servidor ouvindo na porta " + PORT);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
				new Thread(new ClientHandler(clientSocket)).start();
			}
		} catch (IOException e) {
			System.err.println("Erro no servidor: " + e.getMessage());
		}
	}

	private static class ClientHandler implements Runnable {
		private final Socket socket;
		private final Random random = new Random();

		ClientHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
				String message;
				while ((message = in.readLine()) != null) {
					if (message.equalsIgnoreCase("sair")) {
						break;
					}
					String response = responses.get(random.nextInt(responses.size()));
					out.write(response);
					out.newLine();
					out.flush();
				}
			} catch (IOException e) {
				System.err.println("Erro com cliente: " + e.getMessage());
			} finally {
				try {
					socket.close();
					System.out.println("Cliente desconectado.");
				} catch (IOException e) {
					System.err.println("Erro ao fechar socket: " + e.getMessage());
				}
			}
		}
	}
}
