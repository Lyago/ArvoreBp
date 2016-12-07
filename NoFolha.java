import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import controle.ElementoArvoreBp;

class NoFolha extends NoArvore {
	protected final static int ORDEMFOLHA = 4;
	private long[] valores;
	
	public NoFolha() {
		this.chaves = new int[ORDEMFOLHA + 1];
		this.valores = new long[ORDEMFOLHA + 1];
	}

	@SuppressWarnings("unchecked")
	public long getValor(int index) {
		return this.valores[index];
	}

	public void setValor(int index, long value) {
		this.valores[index] = value;;
	}
	
	@Override
	public TipoNoArvore getTipoNo() {
		return TipoNoArvore.LeafNode;
	}
	
	@Override
	public int buscar(int key) {
		for (int i = 0; i < this.getQtdChaves(); ++i) {
			 int cmp = new Integer(this.getChave(i)).compareTo(key);
			 if (cmp == 0) {
				 return i;
			 }
			 else if (cmp > 0) {
				 return -1;
			 }
		}
		
		return -1;
	}
	
	@Override
	public int buscarImprimir(int key) throws IOException {
		
		this.leNo();
		
		for (int i = 0; i < this.getQtdChaves(); ++i) {
			 int cmp = new Integer(this.getChave(i)).compareTo(key);
			 if (cmp == 0) {
				 return i;
			 }
			 else if (cmp > 0) {
				 return -1;
			 }
		}
		
		return -1;
	}
	
	
	public void inserirChave(int chave, long valor) {
		int index = 0;
		while (index < this.getQtdChaves() && new Integer(this.getChave(index)).compareTo(chave) < 0)
			++index;
		this.insertAt(index, chave, valor);
	}
	
	private void insertAt(int index, int chave, long valor) {
		for (int i = this.getQtdChaves() - 1; i >= index; --i) {
			this.setChave(i + 1, this.getChave(i));
			this.setValor(i + 1, this.getValor(i));
		}
		
		
		this.setChave(index, chave);
		this.setValor(index, valor);
		++this.qtdChaves;
	}
	
	

	@Override
	protected NoArvore split() {
		int midIndex = this.getQtdChaves() / 2;
		
		NoFolha novoRNode = new NoFolha();
		for (int i = midIndex; i < this.getQtdChaves(); ++i) {
			novoRNode.setChave(i - midIndex, this.getChave(i));
			novoRNode.setValor(i - midIndex, this.getValor(i));
			this.setChave(i, 0);
			this.setValor(i, -1);
		}
		novoRNode.qtdChaves = this.getQtdChaves() - midIndex;
		this.qtdChaves = midIndex;
		
		return novoRNode;
	}
	
	@Override
	protected NoArvore pushUpChave(int chave, NoArvore filhoEsquerda, NoArvore filhoDireita) {
		throw new UnsupportedOperationException();
	}
	
	protected void escreveNo() throws IOException{
		ArvoreBp.getArvore().seek(this.enderecoArquivo);
		ArvoreBp.getArvore().writeByte(1);
		ArvoreBp.getArvore().writeShort(this.getQtdChaves());
		if(this.getRightSibling() == null){
			ArvoreBp.getArvore().writeLong(-1);
		}else{
			ArvoreBp.getArvore().writeLong(this.getRightSibling().getEnderecoArquivo());
		}
		for (int i = 0; i < chaves.length; i++) {
			ArvoreBp.getArvore().writeInt(this.getChave(i));
			ArvoreBp.getArvore().writeLong(valores[i]);
		}
		
	}
	
	protected NoArvore leNo() throws IOException{
		ArvoreBp.getArvore().seek(this.enderecoArquivo);
		ArvoreBp.getArvore().readByte();
		this.setQtdChaves(ArvoreBp.getArvore().readShort());
		NoFolha prox = new NoFolha();
		prox.setEnderecoArquivo(ArvoreBp.getArvore().readLong());
		this.setRightSibling(prox);
		for (int i = 0; i < chaves.length; i++) {
			this.setChave(i, ArvoreBp.getArvore().readInt());
			this.setValor(i, ArvoreBp.getArvore().readLong());
		}
		return this;
		}
	
	
	public String toString(){
		return this.getEnderecoArquivo()+ "------" + chaves + "-----" + valores ;
	}
	
}
