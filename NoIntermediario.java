import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.RandomAccessFile;

import controle.ElementoArvoreBp;

class NoIntermediario extends NoArvore {
	protected final static int ORDEMINNER = 4;
	protected NoArvore[] filhos; 
	
	public NoIntermediario() {
		this.chaves = new int[ORDEMINNER + 1];
		this.filhos = new NoArvore[ORDEMINNER + 2];
	}
	
	@SuppressWarnings("unchecked")
	public NoArvore getFilhos(int index) {
		return (NoArvore)this.filhos[index];
	}

	public void setFilhos(int index, NoArvore filho) {
		this.filhos[index] = filho;
		if (filho != null)
			filho.setPai(this);
	}
	
	@Override
	public TipoNoArvore getTipoNo() {
		return TipoNoArvore.InnerNode;
	}
	
	@Override
	public int buscar(int chaves) {
		int index = 0;
		for (index = 0; index < this.getQtdChaves(); ++index) {
			int cmp = new Integer(this.getChave(index)).compareTo(chaves);
			if (cmp == 0) {
				return index + 1;
			}
			else if (cmp > 0) {
				return index;
			}
		}
		
		return index;
	}
	
	@Override
	public int buscarImprimir(int chaves) throws IOException {
		this.leNo();
		
		int index = 0;
		for (index = 0; index < this.getQtdChaves(); ++index) {
			int cmp = new Integer(this.getChave(index)).compareTo(chaves);
			if (cmp == 0) {
				return index + 1;
			}
			else if (cmp > 0) {
				return index;
			}
		}
		
		return index;
	}
	
	
	private void insertAt(int index, int chave, NoArvore filhoEsquerda, NoArvore filhoDireita) throws IOException {
		
		for (int i = this.getQtdChaves() + 1; i > index; --i) {
			this.setFilhos(i, this.getFilhos(i - 1));
		}
		for (int i = this.getQtdChaves(); i > index; --i) {
			this.setChave(i, this.getChave(i - 1));
		}
		
		this.setChave(index, chave);
		this.setFilhos(index, filhoEsquerda);
		this.setFilhos(index + 1, filhoDireita);
		this.qtdChaves += 1;
		this.escreveNo();
	}
	

	@Override
	protected NoArvore split() {
		int midIndex = this.getQtdChaves() / 2;
		
		NoIntermediario novoRNode = new NoIntermediario();
		for (int i = midIndex + 1; i < this.getQtdChaves(); ++i) {
			novoRNode.setChave(i - midIndex - 1, this.getChave(i));
			this.setChave(i, 0);
		}
		for (int i = midIndex + 1; i <= this.getQtdChaves(); ++i) {
			novoRNode.setFilhos(i - midIndex - 1, this.getFilhos(i));
			novoRNode.getFilhos(i - midIndex - 1).setPai(novoRNode);
			this.setFilhos(i, null);
		}
		this.setChave(midIndex, 0);
		novoRNode.qtdChaves = this.getQtdChaves() - midIndex - 1;
		this.qtdChaves = midIndex;
		
		
		
		return novoRNode;
	}
	
	@Override
	protected NoArvore pushUpChave(int chave, NoArvore filhoEsquerda, NoArvore filhoDireita) throws IOException {

		int index = this.buscar(chave);
		
	
		this.insertAt(index, chave, filhoEsquerda, filhoDireita);

		
		if (this.isOverflow()) {
			return this.dealOverflow();
		}
		else {
			return this.getPai() == null ? this : null;
		}
	}
	
	protected void escreveNo() throws IOException{
		ArvoreBp.getArvore().seek(this.enderecoArquivo);
		ArvoreBp.getArvore().writeByte(0);
		ArvoreBp.getArvore().writeShort(this.getQtdChaves());
		for (int i = 0; i < chaves.length; i++) {
			ArvoreBp.getArvore().writeInt(this.getChave(i));
		}
		for (int i = 0; i < filhos.length; i++) {
			if(filhos[i] == null){
				ArvoreBp.getArvore().writeLong(-1);
			}else{
				ArvoreBp.getArvore().writeLong(filhos[i].getEnderecoArquivo());
			}	
		}
	}
	
	protected NoArvore leNo() throws IOException{
		ArvoreBp.getArvore().seek(this.enderecoArquivo);
		ArvoreBp.getArvore().readByte();
		this.setQtdChaves(ArvoreBp.getArvore().readShort());
		for (int i = 0; i < chaves.length; i++) {
			this.setChave(i, ArvoreBp.getArvore().readInt());
		}
		for (int i = 0; i < filhos.length; i++) {
			long enderecoFilho = ArvoreBp.getArvore().readLong();
			if(enderecoFilho == -1 ){
				this.setFilhos(i, null);
			}else{	
				NoIntermediario filho = new NoIntermediario();
				filho.setEnderecoArquivo(enderecoFilho);
				this.setFilhos(i, filho);
			}
		}
		return this;
	}
}