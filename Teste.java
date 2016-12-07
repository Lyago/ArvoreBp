import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import controle.Endereco;

public class Teste {
	public static void main(String args[]) throws IOException{
			RandomAccessFile ceps = new RandomAccessFile("cep.dat", "r");
			RandomAccessFile arvore = new RandomAccessFile("arvore.dat", "rw");
			
			ArvoreBp arv = new ArvoreBp(arvore);
			Endereco e = new Endereco();
			while(ceps.getFilePointer() < 300*100){
				
				long endeCep = ceps.getFilePointer(); 
				e.leEndereco(ceps);
				arv.inserir(Integer.parseInt(e.getCep()), endeCep);
			}
			
			ceps.seek(arv.buscar(69909180));
			e.leEndereco(ceps);
			System.out.println(e);
			ceps.seek(arv.buscar(69909250));
			e.leEndereco(ceps);
			System.out.println(e);
			ceps.seek(arv.buscar(69907190));
			e.leEndereco(ceps);
			System.out.println(e);
			ceps.seek(arv.buscar(69907060));
			e.leEndereco(ceps);
			System.out.println(e);
			ceps.seek(arv.buscar(69907110));
			e.leEndereco(ceps);
			System.out.println(e);
			ceps.seek(arv.buscar(69909170));
			e.leEndereco(ceps);
			System.out.println(e);
	}
}
