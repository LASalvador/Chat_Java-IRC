
import java.io.*; 
import java.net.*; 
import java.util.Random;
import java.util.Scanner; 

public class Client 
{ 
	final static int porta = 1234;
	

	public static void main(String args[]) throws UnknownHostException, IOException 
	{ 
		Scanner scn = new Scanner(System.in);
		Random random = new Random();
		String ip, nickName;
		int chave;
		System.out.println("Insira o IP do sevidor: ");
		ip = scn.nextLine();
		System.out.println("Insira um Nick Name: ");
		nickName = scn.nextLine();
		System.out.println("Se você quiser mandar mensagem pra todos digite:\n Mensagem #all \n Exemplo: Ola #all\n");
		System.out.println("Se você quiser mandar mensagem privada digite:\n Mensagem #NickDoDestinatario \n Exemplo: Ola #Lucas\n");
		
		chave = random.nextInt(26);
		
		// Configurando endereço do servidor
		InetAddress endereco = InetAddress.getByName(ip); 
		
		// Fazendo conexão
		Socket s = new Socket(endereco, porta); 
		
		// Obtendo output e input Stream
		DataInputStream dis = new DataInputStream(s.getInputStream()); 
		DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
		dos.writeUTF(nickName); 
		dos.writeUTF(String.valueOf(chave));

		// Thread de enviar Mensagem
		Thread enviaMensagem = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 
				while (true) { 

					 // Lê mensagem digitada
					String msg = scn.nextLine();
					// Tratando String para criptografar
					// Descobrindo posicao do # e tamanho do string
					int posDest = msg.indexOf("#");
					int tamanho = msg.length();
					String conteudoMsg = msg.substring(0,posDest-1);
					String destinatarioMsg = msg.substring(posDest+1,tamanho);

					// Criptografando e arrumando msg para servidor
					String msgCripta = Criptografa.encriptar(chave, conteudoMsg);

					String msgFinal = msgCripta + "#" + destinatarioMsg;
					
					try { 
						// Envia mensagem
						dos.writeUTF(msgFinal); 
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
						String chave = dis.readUTF();
						String msg = dis.readUTF();

						System.out.println(msg);
						String msgEncripta = Criptografa.decriptar(Integer.parseInt(chave), msg);
						System.out.println(msgEncripta);
						
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
