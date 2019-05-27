package br.com.fatec.Servidor;

import java.io.*; 
import java.util.*; 
import java.net.*; 

// Classe Server
public class Server 
{ 

	// Vetor para armazenar lista de clientes
	static Vector<ClientHandler> ar = new Vector<>(); 
	
	// Contador para clientes
	static int i = 0; 

	public static void main(String[] args) throws IOException 
	{ 
		// Servidor ouvindo na porta 1234
		ServerSocket ss = new ServerSocket(1234); 
		
		Socket s; 
		System.out.println("Servidor online");
		// Rodando em loop infinito para receber conexões de clientes
		while (true) 
		{ 
			// Aceitando requisição
			s = ss.accept(); 

			System.out.println("Requisição de novos clientes : " + s); 
			
			// Obtendo input e output Stream desse cliente
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
			String nickName = dis.readUTF();
			String chave = dis.readUTF();
			

			// Criando um novo handler para este cliente
			ClientHandler clientConexao = new ClientHandler(s,nickName, dis, dos, chave); 

			// Criando uma thread para esse cliente
			Thread t = new Thread(clientConexao); 
			
			System.out.println("Cliente adicionado a lista"); 

			// Adicionando cliente na lista de cliente
			ar.add(clientConexao); 

			// Iniciando a thread
			t.start(); 

			// incrementa i para novo cliente. 
			i++; 

		} 
	} 
} 

// Classe ClientHandler 
class ClientHandler implements Runnable 
{ 
	Scanner scn = new Scanner(System.in); 
	private String nome; 
	final DataInputStream dis; 
	final DataOutputStream dos; 
	Socket s; 
	boolean isloggedin; 
	String chave;
	
	// Construtor
	public ClientHandler(Socket s, String nome,	DataInputStream dis, DataOutputStream dos, String chave) { 
		this.dis = dis; 
		this.dos = dos; 
		this.nome = nome; 
		this.s = s; 
		this.isloggedin=true;
		this.chave = chave;
	} 

	@Override
	public void run() { 

		String recebido; 
		while (true) 
		{ 
			try
			{ 
				// Recebendo a string
				recebido = dis.readUTF(); 
				
				System.out.println(recebido); 
				
				if(recebido.equals("logout")){ 
					this.isloggedin=false; 
					this.s.close(); 
					break; 
				} 
				
				// Quebrando a string em msg e destinatário
				StringTokenizer st = new StringTokenizer(recebido, "#"); 
				String conteudo = st.nextToken(); 
				String destinatario = st.nextToken(); 

				System.out.println("Destino: "+destinatario);

				if(destinatario.equals("all")) {
					for (ClientHandler mc : Server.ar) 
					{	 
						mc.dos.writeUTF(this.chave);
						mc.dos.writeUTF(this.nome+" : "+conteudo); 
					}
		

				} else {
				// procurando receptor na lista
				for (ClientHandler mc : Server.ar) 
				{ 
					//se o receptor for encontrado manda na lista
					if (mc.nome.equals(destinatario) && mc.isloggedin==true) 
					{ 
						mc.dos.writeUTF(this.chave);
						mc.dos.writeUTF(this.nome+" : "+conteudo); 
						break; 
					} 
				} 
				}
			} catch (IOException e) { 
				
				e.printStackTrace(); 
			} 
			
		} 
		try
		{ 
			// Fechando conexões
			this.dis.close(); 
			this.dos.close(); 
			
		}catch(IOException e){ 
			e.printStackTrace(); 
		} 
	} 
} 
