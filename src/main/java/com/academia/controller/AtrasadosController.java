package com.academia.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.academia.dao.ConexaoDB;
import com.academia.model.Aluno;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class AtrasadosController implements Initializable {

    private Connection con = ConexaoDB.getConnection();

    @FXML
    private Button listar;

    @FXML
    private TableView<Aluno> tblAlunosAtrasados;

    @FXML
    private TableColumn<Aluno, Integer> matricula;

    @FXML
    private TableColumn<Aluno, String> name;

    @FXML
    private TableColumn<Aluno, String> contat;

    @FXML
    private TableColumn<Aluno, Double> mensal;

    @FXML
    private TableColumn<Aluno, String> situacao;

    @FXML
    private TableColumn<Aluno, Integer> diasAtraso;

    ObservableList<Aluno> alunosAtrasados = FXCollections.observableArrayList();

    void listarAtrasados() throws ParseException{

        Aluno atrasoAluno = new Aluno();

        try {

            String sql = "SELECT * FROM alunos";
            int totalAtrasados = 0;

            PreparedStatement std = con.prepareStatement(sql);
            ResultSet result = std.executeQuery();

            while(result.next()){

                atrasoAluno.setMatricula(result.getInt("matricula"));
                atrasoAluno.setVencimento(result.getString("vencimento"));

                LocalDate hoje = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String hojeFormatado = hoje.format(formatter);

                SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
                Date vencimento = formataData.parse(atrasoAluno.getVencimento());
                Date dataDeHoje = formataData.parse(hojeFormatado);

                long diffEmMil = Math.abs(dataDeHoje.getTime() - vencimento.getTime());
                long diff = TimeUnit.DAYS.convert(diffEmMil, TimeUnit.MILLISECONDS);

                atrasoAluno.setAtraso((int) diff);

                if(dataDeHoje.after(vencimento)){

                    String atrasado = "UPDATE alunos SET diasatraso = (?), atrasados = (?) WHERE matricula = (?)";
                    alunosAtrasados.add(new Aluno(result.getInt("matricula"), result.getString("nome"),result.getString("endereco"),  result.getString("bairro"), result.getString("contato"), result.getString("email"), result.getInt("mensalidade"), result.getString("vencimento"), result.getString("atrasados"), result.getInt("diasatraso")));

                    PreparedStatement st = con.prepareStatement(atrasado);

                    st.setInt(1, atrasoAluno.getAtraso());
                    st.setString(2, "Em atraso");
                    st.setInt(3, atrasoAluno.getMatricula());
                    st.execute();
                    st.close();

                    matricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
                    name.setCellValueFactory(new PropertyValueFactory<>("nome"));
                    contat.setCellValueFactory(new PropertyValueFactory<>("contato"));
                    mensal.setCellValueFactory(new PropertyValueFactory<>("mensalidade"));
                    situacao.setCellValueFactory(new PropertyValueFactory<>("atrasados"));
                    diasAtraso.setCellValueFactory(new PropertyValueFactory<>("atraso"));

                    tblAlunosAtrasados.setItems(alunosAtrasados);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            listarAtrasados();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

