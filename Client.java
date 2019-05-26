// Classe do Cliente

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

public class Client 
{ 
	final static int porta = 1234; 

	public static void main(String args[]) throws UnknownHostException, IOException 
	{ 
		Scanner scn = new Scanner(System.in); 
		
		// Configurando endereço do servidor
		InetAddress endereco = InetAddress.getByName("localhost"); 
		
		// Fazendo conexão
		Socket s = new Socket(endereco, porta); 
		
		// Obtendo output e input Stream
		DataInputStream dis = new DataInputStream(s.getInputStream()); 
		DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

		// Thread de enviar Mensagem
		Thread enviaMensagem = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 
				while (true) { 

					 // Lê mensagem digitada
					String msg = scn.nextLine(); 
					
					try { 
						// Envia mensagem
						dos.writeUTF(msg); 
					} catch (IOException e) { 
						e.printStackTrace(); 
					} 
				} 
			} 
		}); 
		
		// Thread de ler mensagem
		Thread leMensagem = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 

				while (true) { 
					try { 
						// Recebe mensagem
						String msg = dis.readUTF(); 
						System.out.println(msg); 
					} catch (IOException e) { 

						e.printStackTrace(); 
					} 
				} 
			} 
		}); 

		enviaMensagem.start(); 
		leMensagem.start(); 

	} 
} 
