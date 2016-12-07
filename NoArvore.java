import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import controle.ElementoArvoreBp;
import controle.GerenciadorDeArquivo;

enum TipoNoArvore {
	InnerNode,
	LeafNode
}

abstract class NoArvore  {
	protected int[] chaves;
	protected int qtdChaves;
	protected NoArvore NoPai;
	protected NoArvore leftSibling;
	protected NoArvore rightSibling;
	protected long enderecoArquivo;
	

	protected NoArvore() {
		this.qtdChaves = 0;
		this.NoPai = null;
		this.leftSibling = null;
		this.rightSibling = null;
	}
	
	
	
	
	public NoArvore getNoPai() {
		return NoPai;
	}




	public void setNoPai(NoArvore noPai) {
		NoPai = noPai;
	}




	public void setQtdChaves(int qtdChaves) {
		this.qtdChaves = qtdChaves;
	}




	public long getEnderecoArquivo() {
		return this.enderecoArquivo;
	}


	public void setEnderecoArquivo(long enderecoArquivo) {
		this.enderecoArquivo = enderecoArquivo;
	}

	public int getQtdChaves() {
		return this.qtdChaves;
	}
	
	@SuppressWarnings("unchecked")
	public int getChave(int index) {
		return this.chaves[index];
	}

	public void setChave(int index, int chave) {
		this.chaves[index] = chave;
	}

	public NoArvore getPai() {
		return this.NoPai;
	}

	public void setPai(NoArvore pai) {
		this.NoPai = pai;
	}	
	
	public abstract TipoNoArvore getTipoNo();
	
	
	
	public abstract int buscar(int chave);
	
	
	
	
	public boolean isOverflow() {
		return this.getQtdChaves() == this.chaves.length;
	}
	
	public NoArvore dealOverflow() throws IOException {
		int midIndex = this.getQtdChaves() / 2;
		int chaveUp = this.getChave(midIndex);
		
		NoArvore novoRNode = this.split();
				
		if (this.getPai() == null) {
			this.setPai(new NoIntermediario());
			this.getPai().setEnderecoArquivo(ArvoreBp.getArvore().length());
			this.getPai().escreveNo();
		}
		novoRNode.setPai(this.getPai());
		
		
		novoRNode.setLeftSibling(this);
		novoRNode.setRightSibling(this.rightSibling);
		if (this.getRightSibling() != null)
			this.getRightSibling().setLeftSibling(novoRNode);
		this.setRightSibling(novoRNode);
		
		novoRNode.setEnderecoArquivo(ArvoreBp.getArvore().length());
		novoRNode.escreveNo();
		
		return this.getPai().pushUpChave(chaveUp, this, novoRNode);
	}
	
	protected boolean isLeaf() throws IOException{
		ArvoreBp.getArvore().seek(this.getEnderecoArquivo());
		byte byte0 = ArvoreBp.getArvore().readByte();
		if(byte0 == 1){
			return true;
		}
		return false;
	}
	
	protected abstract int buscarImprimir(int chaves)throws IOException;
	
	protected abstract NoArvore split();
	
	protected abstract NoArvore pushUpChave( int chave, NoArvore filhoEsquerda, NoArvore nodeDireita)throws IOException;
	
	protected abstract void escreveNo() throws IOException;
	
	protected abstract NoArvore leNo() throws IOException;
	
	public String toString(){
		 return null;
	}
	
	

	public void setLeftSibling(NoArvore sibling) {
		this.leftSibling = sibling;
	}

	public NoArvore getRightSibling() {
		if (this.rightSibling != null && this.rightSibling.getPai() == this.getPai())
			return this.rightSibling;
		return null;
	}

	public void setRightSibling(NoArvore silbling) {
		this.rightSibling = silbling;
	}
	
}