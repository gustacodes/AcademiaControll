package com.academia.controller;
import com.academia.dao.ConexaoDB;
import com.academia.model.Aluno;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class TelaPrincipalController implements Initializable {


    @FXML
    private TableView<Aluno> tblAlunos;

    @FXML
    private TableColumn<Aluno, String> contato;

    @FXML
    private TableColumn<Aluno, String> email;

    @FXML
    private TableColumn<Aluno, Integer> matricula;

    @FXML
    private TableColumn<Aluno, String> endereco;

    @FXML
    private TableColumn<Aluno, Float> mensalidade;

    @FXML
    private TableColumn<Aluno, String> aluno;

    @FXML
    private TableColumn<Aluno, String> bairro;

    @FXML
    private TableColumn<Aluno, String> vencimento;

    @FXML
    private Button botaoAlterar;

    @FXML
    private Button botaoAtraso;

    @FXML
    private Button botaoBusca;

    @FXML
    private Button botaoCadastro;

    @FXML
    private Button botaoPagamento;

    @FXML
    private TextField txtBusca;

    //Chama a conexão com o Banco de Dados
    private Connection con = ConexaoDB.getConnection();

    @FXML
    void alterarAluno(ActionEvent event) {
        FXMLLoader fx = new FXMLLoader(TelaPrincipalController.class.getResource("/com/academia/alteraCadastro.fxml"));
        try {
            Scene s = new Scene(fx.load());
            Stage st = new Stage();
            st.setTitle("Atualizar Cadastro");
            st.setResizable(false);
            st.setScene(s);
            st.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void pagamentoAluno(ActionEvent event) {
        Aluno al = tblAlunos.getSelectionModel().getSelectedItem();

        if(al == null){
            Alert erro = new Alert(AlertType.ERROR);
            erro.setTitle("Error");
            erro.setHeaderText("Nenhum aluno foi selecionado");
            erro.setContentText("Por favor, selecione um aluno para efetivar o pagamento");
            erro.showAndWait();
        } else {
            Alert confirma = new Alert(AlertType.CONFIRMATION);
            confirma.setTitle("Confirmação de pagamento");
            confirma.setHeaderText("Confirme os dados abaixo");
            confirma.setContentText("Aluno: " + al.getNome() + "\n" + "Mensalidade: " + al.getMensalidade());

            Optional<ButtonType> escolha = confirma.showAndWait();

            if(escolha.isPresent() && escolha.get() == ButtonType.OK){

                String sql = "SELECT * FROM alunos WHERE matricula = (?)";
                String vencimento = null;

                try {

                    PreparedStatement std = con.prepareStatement(sql);
                    std.setInt(1, al.getMatricula());
                    ResultSet rs = std.executeQuery();

                    while(rs.next()){
                        vencimento = rs.getString("vencimento");
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dateToday = LocalDate.parse(vencimento, formatter);
                    LocalDate dataHoje = dateToday.plusMonths(1);
                    String dataDeHoje = dataHoje.format(formatter);

                    String atualizaData = "UPDATE alunos SET vencimento = (?), atrasados = (?), diasatraso = (?) WHERE matricula = (?)";

                    PreparedStatement novaData = con.prepareStatement(atualizaData);

                    novaData.setString(1, dataDeHoje);
                    novaData.setString(2, "Pago");
                    novaData.setInt(3, 0);
                    novaData.setInt(4, al.getMatricula());

                    novaData.execute();
                    novaData.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @FXML
    void atrasoAluno(ActionEvent event) {

        FXMLLoader fx = new FXMLLoader(TelaPrincipalController.class.getResource("/com/academia/atrasados.fxml"));
        try {
            Scene s = new Scene(fx.load());
            Stage st = new Stage();
            st.setTitle("Atrasados");
            st.setScene(s);
            st.setResizable(false);
            st.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void financeiro(ActionEvent event){
        FXMLLoader fx = new FXMLLoader(TelaPrincipalController.class.getResource("/com/academia/financeiro.fxml"));
        try {
            Scene s = new Scene(fx.load());
            Stage st = new Stage();
            st.setTitle("Financeiro");
            st.setScene(s);
            st.setResizable(false);
            st.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ObservableList<Aluno> alunos = FXCollections.observableArrayList();

    //Chamada para formulário de cadastro
    @FXML
    void cadastroAluno(ActionEvent event) {

        FXMLLoader fx = new FXMLLoader(TelaPrincipalController.class.getResource("/com/academia/cadastro.fxml"));
        try {
            Scene s = new Scene(fx.load());
            Stage st = new Stage();
            st.setTitle("Cadastro");
            st.setScene(s);
            st.setResizable(false);
            st.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Função para buscar alunos cadastrados
    private ObservableList<Aluno> busca(){
        ObservableList<Aluno> buscaAluno = FXCollections.observableArrayList();

        for(int i = 0; i < alunos.size(); i++){

            if(alunos.get(i).getNome().toLowerCase().contains(txtBusca.getText().toLowerCase())){
                buscaAluno.add(alunos.get(i));
            }
        }

        return buscaAluno;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            //Faz a conexão com o Banco de dados e executa a Query abaixo, chamando dos os alunos cadastrados
            ResultSet result = con.createStatement().executeQuery("SELECT * FROM alunos");

            //Leitura de todos os alunos matriculados. Enquanto houver aluno, ele vai retornar dentro do laço
            while(result.next()){
                alunos.add(new Aluno(result.getInt("matricula"), result.getString("nome"),  result.getString("endereco"),  result.getString("bairro"), result.getString("contato"), result.getString("email"), result.getFloat("mensalidade"), result.getString("vencimento"), result.getString("atrasados"), 0));
            }

        } catch (Exception e) {
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Error");
            erro.setHeaderText("Erro na consulta");
            erro.setContentText("Falha ao exibir listagem de alunos");
            erro.showAndWait();
        }

        //Inserindo todos os alunos matriculados na tabela
        matricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        aluno.setCellValueFactory(new PropertyValueFactory<>("nome"));
        endereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        bairro.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        contato.setCellValueFactory(new PropertyValueFactory<>("contato"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        mensalidade.setCellValueFactory(new PropertyValueFactory<>("mensalidade"));
        vencimento.setCellValueFactory(new PropertyValueFactory<>("vencimento"));

        tblAlunos.setItems(alunos);

        //Faz uma busca na tabela em tempo real no qual é digitado o nome do aluno
        txtBusca.setOnKeyReleased((KeyEvent e ) -> {
            tblAlunos.setItems(busca());
        });
    }

}
