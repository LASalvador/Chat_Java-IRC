
public class Criptografa {
   public static String encriptar(int chave, String texto) {
      StringBuffer criptografado= new StringBuffer(); 
  
        for (int i=0; i<texto.length(); i++) 
        { 
            if(texto.charAt(i) == 32){
              continue;
            }
            if (Character.isUpperCase(texto.charAt(i))) 
            { 
                char ch = (char)(((int)texto.charAt(i) + chave - 65) % 26 + 65); 
                criptografado.append(ch); 
            } 
            else
            { 
                char ch = (char)(((int)texto.charAt(i) + chave - 97) % 26 + 97); 
                criptografado.append(ch); 
            } 
        } 
        return criptografado.toString(); 
   }

   public static String decriptar(int chave, String textoCifrado) {
      String mensagemDecriptografada = "";
      char ch;
         for(int i = 0; i < textoCifrado.length(); ++i){
         ch = textoCifrado.charAt(i);
         if(ch == ' '){
          mensagemDecriptografada += " ";
          continue;
         }
         if(ch >= 'a' && ch <= 'z'){
               ch = (char)(ch - chave);
               
               if(ch < 'a'){
                   ch = (char)(ch + 'z' - 'a' + 1);
               }
               
               mensagemDecriptografada += ch;
           }
           else if(ch >= 'A' && ch <= 'Z'){
               ch = (char)(ch - chave);
               
               if(ch < 'A'){
                   ch = (char)(ch + 'Z' - 'A' + 1);
               }
               
               mensagemDecriptografada += ch;
           }
           else {
            mensagemDecriptografada += ch;
           }
      }
      return mensagemDecriptografada;
   }
}

