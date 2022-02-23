package game.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro {
	private int qtdLinhas;
	private int qtdColunas;
	private int qtdMinas;
	
	private final List<Campo> listaCampos = new ArrayList<>();

	public Tabuleiro(int qtdLinhas, int qtdColunas, int qtdMinas) {
		this.qtdLinhas = qtdLinhas;
		this.qtdColunas = qtdColunas;
		this.qtdMinas = qtdMinas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	public void abrirCampo(int linha, int coluna) {
		listaCampos.parallelStream()
		.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		.findFirst()
		.ifPresent(c -> c.abrir());
	}
	
	public void marcarCampo(int linha, int coluna) {
		listaCampos.parallelStream()
		.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		.findFirst()
		.ifPresent(c -> c.alternarMarcao());
	}
	
	private void gerarCampos() {
		for (int linha = 0; linha < qtdLinhas; linha++) {
			for (int coluna = 0; coluna < qtdColunas; coluna++) {
				listaCampos.add(new Campo(linha,coluna));
			}
		}
	}
	
	private void associarVizinhos() {
		for(Campo c1: listaCampos) {
			for(Campo c2: listaCampos) {
				c1.adicionarVizinho(c2);
			}
		}
	}
	
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		do {
			minasArmadas = listaCampos.stream().filter(minado).count();
			//gera um indice alatorio da lista
			int campoAleatorio = (int)(Math.random()*listaCampos.size());
			listaCampos.get(campoAleatorio).minar();
		} while (minasArmadas < qtdMinas);
	}
	
	public boolean jogoGanho() {
		return listaCampos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciarJogo() {
		listaCampos.stream().forEach(c -> c.reset());
		sortearMinas();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i =0;
		for (int l = 0; l < qtdLinhas; l++) {
			for (int c = 0; c < qtdColunas; c++) {
				sb.append(" ");
				sb.append(listaCampos.get(i));
				sb.append(" ");
				i++;
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
