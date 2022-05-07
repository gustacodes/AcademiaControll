package com.academia.controller;
import com.academia.dao.ConexaoDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 *   @author Gustavo Henrique
 *
 *           Tela de login
 *
 */

public class LoginController {

    @FXML
    private Button botaoLogin;

    @FXML
    private TextField campoSenha;

    @FXML
    private TextField campoUsuario;

    @FXML
    private Hyperlink criarConta;

    //Função para redefinição de senha
    @FXML
    void redSenha(ActionEvent event){
        FXMLLoader fx = new FXMLLoader(LoginController.class.getResource("/com/academia/recuperaSenha.fxml"));

        try {
            Scene s = new Scene(fx.load());
            Stage st = new Stage();
            st.setTitle("Redefinição de senha");
            st.setScene(s);
            st.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Connection conexao = ConexaoDB.getConnection();

    //Login para acessar o programa
    @FXML
    void fazerLogin(ActionEvent event) {

        //SELECT para verificar se o e-mail consta no BD
        String login = "SELECT * FROM funcionario WHERE email = (?)";
        //Captura o que o usuário digitou no campo E-mail
        String usuario = campoUsuario.getText();
        //Captura o que o usuário digitou no campo Senha
        String senha = campoSenha.getText();
        //Abaixo duas Strings vazias para receber o e-mail e senha do BD
        String pass = "";
        String email = "";

        try {

            //Conectando ao BD
            PreparedStatement statement = conexao.prepareStatement(login);
            //Informando que quero o e-mail do BD
            statement.setString(1, usuario);
            //Executando
            ResultSet result = statement.executeQuery();

            //Comando while vai retornar o e-mail e senha desse usuário. Que serão armazenadas nas variáveis vazias criadas acima
            while(result.next()){
                pass = result.getString("senha");
                email = result.getString("email");
            }

            //Verifico se os campos estão vazios
            if(usuario.equals("") || senha.equals("")){
                Alert logar = new Alert(Alert.AlertType.WARNING);
                logar.setTitle("Erro ao logar");
                logar.setHeaderText("Falha no acesso");
                logar.setContentText("O campo usuário/senha não podem estar vazios.");
                logar.showAndWait();

            } else {

                //Aqui verifico se o e-mail e a senha informados nos campos são iguais ao que estão no BD
                if(usuario.equals(email) && senha.equals(pass)){

                    //Se o usuário for cadastrado, ele entra aqui
                    FXMLLoader fx = new FXMLLoader(LoginController.class.getResource("/com/academia/main.fxml"));
                    try {
                        Scene s = new Scene(fx.load());
                        Stage st = new Stage();
                        st.setTitle("Principal");
                        st.setResizable(false);
                        st.setScene(s);
                        st.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    //Caso não bata a senha ou e-mail, vai cair aqui
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("OPS!");
                    alerta.setContentText("Usuário ou senha incorretos." + "\nPor favor, verifique seus dados e tente novamente!");
                    alerta.setHeaderText("Erro ao acessar o sistema");
                    alerta.showAndWait();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar funcionário");
        }

    }

}