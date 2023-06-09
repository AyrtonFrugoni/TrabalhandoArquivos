package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Informe o caminho completo do arquivo .csv: ");
		String sourceFile = scanner.nextLine();

		try {
			// Obtendo o diretório do arquivo de origem
			Path sourcePath = Paths.get(sourceFile);
			String outputFolder = sourcePath.getParent().toString();

			// Criando a pasta de saída se não existir
			Path outputPath = Paths.get(outputFolder, "out");
			if (!Files.exists(outputPath)) {
				Files.createDirectories(outputPath);
			}

			// Criando o caminho completo para o arquivo de saída
			String outputFile = outputPath.resolve("summary.csv").toString();

			// Criando o arquivo de saída
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

			// Criando um mapa para armazenar os totais individuais
			Map<String, Double> itemTotals = new HashMap<>();

			// Lendo o arquivo de origem
			BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
			String line;
			while ((line = reader.readLine()) != null) {
				// Dividindo a linha em nome, preço unitário e quantidade
				String[] parts = line.split(",");
				String name = parts[0];
				double price = Double.parseDouble(parts[1]);
				int quantity = Integer.parseInt(parts[2]);

				// Calculando o valor total
				double total = price * quantity;

				// Verificando se o item já está no mapa de totais
				if (itemTotals.containsKey(name)) {
					// Se estiver, adiciona o valor total ao valor existente
					double currentTotal = itemTotals.get(name);
					itemTotals.put(name, currentTotal + total);
				} else {
					// Se não estiver, adiciona o item com o valor total
					itemTotals.put(name, total);
				}
			}

			// Escrevendo os itens e seus totais no arquivo de saída
			for (Map.Entry<String, Double> entry : itemTotals.entrySet()) {
				writer.println(entry.getKey() + "," + String.format("%.2f", entry.getValue()));
			}

			// Fechando os arquivos
			reader.close();
			writer.close();

			System.out.println("Arquivo 'summary.csv' gerado com sucesso em '" + outputFile + "'.");
		} catch (IOException e) {
			System.err.println("Ocorreu um erro ao processar o arquivo: " + e.getMessage());
		} finally {
			scanner.close();
		}
	}
}
