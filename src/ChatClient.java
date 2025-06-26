import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
	private static final String SERVER_HOST = "localhost";
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) {
		try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				Scanner scanner = new Scanner(System.in)) {
			System.out.println("Conectado ao servidor. Digite suas mensagens (digite 'sair' para encerrar):");

			while (true) {
				System.out.print("> ");
				String userInput = scanner.nextLine();
				out.write(userInput);
				out.newLine();
				out.flush();

				if (userInput.equalsIgnoreCase("sair")) {
					break;
				}

				String response = in.readLine();
				System.out.println("Servidor: " + response);
			}

		} catch (IOException e) {
			System.err.println("Erro no cliente: " + e.getMessage());
		}
	}
}
