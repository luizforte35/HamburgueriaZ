package com.example.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

	private int quantidade = 1;
	private static final int PRECO_BASE = 20;
	private static final int PRECO_BACON = 2;
	private static final int PRECO_QUEIJO = 2;
	private static final int PRECO_ONION = 3;

	private EditText editTextNome;
	private CheckBox checkBoxBacon, checkBoxQueijo, checkBoxOnion;
	private TextView textViewQuantidade, textViewResumo;
	private Button btnMais, btnMenos, btnEnviarPedido;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Inicializando os componentes
		editTextNome = findViewById(R.id.editTextNome);
		checkBoxBacon = findViewById(R.id.checkBoxBacon);
		checkBoxQueijo = findViewById(R.id.checkBoxQueijo);
		checkBoxOnion = findViewById(R.id.checkBoxOnion);
		textViewQuantidade = findViewById(R.id.textViewContador);
		textViewResumo = findViewById(R.id.textViewResumoPedido);
		btnMais = findViewById(R.id.buttonMais);
		btnMenos = findViewById(R.id.buttonMenos);
		btnEnviarPedido = findViewById(R.id.buttonFazerPedido);

		// Configurando os botões
		btnMais.setOnClickListener(v -> somar());
		btnMenos.setOnClickListener(v -> subtrair());
		btnEnviarPedido.setOnClickListener(v -> enviarPedido());
	}

	// Função para incrementar a quantidade
	private void somar() {
		quantidade++;
		textViewQuantidade.setText(String.valueOf(quantidade));
	}

	// Função para decrementar a quantidade (mínimo 1)
	private void subtrair() {
		if (quantidade > 1) {
			quantidade--;
			textViewQuantidade.setText(String.valueOf(quantidade));
		}
	}

	// Função para calcular o preço total do pedido
	private int calcularPrecoTotal() {
		int precoTotal = PRECO_BASE * quantidade;

		if (checkBoxBacon.isChecked()) precoTotal += PRECO_BACON * quantidade;
		if (checkBoxQueijo.isChecked()) precoTotal += PRECO_QUEIJO * quantidade;
		if (checkBoxOnion.isChecked()) precoTotal += PRECO_ONION * quantidade;

		return precoTotal;
	}

	// Função para montar o resumo do pedido e enviar por e-mail
	private void enviarPedido() {
		String nomeCliente = editTextNome.getText().toString().trim();
		if (nomeCliente.isEmpty()) {
			editTextNome.setError("Digite seu nome!");
			return;
		}

		// Verificando quais adicionais foram selecionados
		boolean temBacon = checkBoxBacon.isChecked();
		boolean temQueijo = checkBoxQueijo.isChecked();
		boolean temOnion = checkBoxOnion.isChecked();

		// Construindo o resumo do pedido
		String resumo = "Nome do cliente: " + nomeCliente + "\n" +
				"Tem Bacon? " + (temBacon ? "Sim" : "Não") + "\n" +
				"Tem Queijo? " + (temQueijo ? "Sim" : "Não") + "\n" +
				"Tem Onion Rings? " + (temOnion ? "Sim" : "Não") + "\n" +
				"Quantidade: " + quantidade + "\n" +
				"Preço final: R$ " + calcularPrecoTotal();

		// Exibir o resumo na tela
		textViewResumo.setText(resumo);

		// Criando Intent para enviar o pedido por e-mail
		Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
		intentEmail.setData(Uri.parse("mailto:")); // Apenas apps de e-mail responderão a esse Intent
		intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nomeCliente);
		intentEmail.putExtra(Intent.EXTRA_TEXT, resumo);

		// Verifica se existe um aplicativo de e-mail instalado antes de tentar abrir
		if (intentEmail.resolveActivity(getPackageManager()) != null) {
			startActivity(intentEmail);
		}

	}
}
