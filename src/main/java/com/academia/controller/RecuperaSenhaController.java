package com.academia.controller;
import com.academia.dao.ConexaoDB;
import com.academia.model.RecuperaSenha;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 *   @author Gustavo Henrique
 *
 *           Função para recuperar senha
 *
 */

public class RecuperaSenhaController implements Initializable {

    @FXML
    private Button botaoRecuperaSenha;

    @FXML
    private TextField campoRecuperaSenha;

    @FXML
    private TextField novaSenha;

    @FXML
    private TextField novaSenhaRep;

    //Conexão com o BD
    private Connection conexao = ConexaoDB.getConnection();

    private int codigo;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    //Função para recuperar senha (trocar)
    @FXML
    void recuperaSenha(ActionEvent event) {

        //Instancio a classe RecuperaSenha para ter acesso ao seus atributos e metódos
        RecuperaSenha senha = new RecuperaSenha();

        //Um SELECT no BD para ele me retornar o e-mail e senha
        String buscaEmail = "SELECT email, senha, nome FROM funcionario WHERE email = (?) OR senha = (?) OR nome = (?)";

        //Aqui capturo o que foi digitado no campo E-MAIL, SENHA e NOVA SENHA. E mais abaixo crio 2 strings vazias para receber os dados do banco
        senha.setEmail(campoRecuperaSenha.getText());
        senha.setNSenha(novaSenha.getText());
        senha.setNSenhaRep(novaSenhaRep.getText());
        senha.setCapturaEmail("");
        senha.setCapturaSenha("");
        String nome = "";
        String nomeAux = "";

        try {

            //Verifico se algum dos campos estão vazios. Caso estejam todos preenchidos, cairá no ELSE
            if(senha.getEmail().isEmpty() || senha.getNSenha().isEmpty() || senha.getNSenhaRep().isEmpty()){
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro ao refedefinir senha");
                erro.setHeaderText("Um dos campos está vazio.");
                erro.setContentText("Por favor, insira todos os dados");
                erro.showAndWait();

            } else {

                //Conecto com o BD e insiro o comando SQL que está na variavel buscaEmail
                PreparedStatement statement = conexao.prepareStatement(buscaEmail);

                //Aqui informo que na primeira 'Setada' na 1 posição da ordem no comando SQL (buscaEmail) vai retornar o e-mail
                statement.setString(1, senha.getEmail());
                //E aqui retorna a senha
                statement.setString(2, senha.getNSenha());
                //Retorno o nome
                statement.setString(3, nome);
                //Aqui executo o comando SQL
                ResultSet rs = statement.executeQuery();

                //No laço while ele vai retornar o e-mail e a senha e Setar para as 2 Strings vazias que eu tinha declarado acima
                while(rs.next()){
                    senha.setCapturaEmail(rs.getString("email"));
                    senha.setCapturaSenha(rs.getString("senha"));;
                    nomeAux = rs.getString("nome");
                }

                //Aqui verifico se a nova senha no qual ele queira trocar não é a mesma já cadastrada
                if(senha.getNSenha().equalsIgnoreCase(senha.getCapturaSenha())){
                    Alert erro = new Alert(Alert.AlertType.ERROR);
                    erro.setTitle("Erro ao refedefinir senha");
                    erro.setHeaderText("Nova senha não aceita");
                    erro.setContentText("Sua nova senha não pode ser igual a atual.");
                    erro.showAndWait();

                } else {

                    //Aqui verifico se o email informado é igual ao cadastrado no BD
                    if(senha.getEmail().equals(senha.getCapturaEmail())){

                        //Aqui verifico se as novas senhas são iguais. Há um campo para nova senha, e outro para repetí-la para confirmação
                        if(senha.getNSenha().equalsIgnoreCase(senha.getNSenhaRep())){

                            //Aqui estou definindo acesso ao e-mail usado para enviar o código de recuperação de senha
                            String meuEmail = "porajureg@gmail.com";
                            String minhaSenha = "jureg15421542";

                            //Instancimento do objeto usado para conectar a porta da Gmail
                            MultiPartEmail mail = new MultiPartEmail();

                            //Aqui são os requerimentos que a aplicação deve ter para conexão com a Gmail
                            mail.setHostName("smtp.gmail.com");
                            mail.setSslSmtpPort("465");
                            mail.setStartTLSRequired(true);
                            mail.setStartTLSEnabled(true);
                            mail.setSSLOnConnect(true);

                            //Aqui verifico se minha conta na Gmail existe ou não para poder continuar
                            mail.setAuthenticator(new DefaultAuthenticator(meuEmail, minhaSenha));

                            try {

                                //Aqui instancio um objeto do tipo Random para gerar números aleatórios
                                Random rand = new Random();

                                //Aqui utilizo a função random para me gerar um número aleatório para matrícula do aluno
                                setCodigo(rand.nextInt(10000) + 1);
                                //Informo qual e-mail que vai ser apresentado no envio
                                mail.setFrom(meuEmail);
                                //Mensagem do e-mail
                                mail.setSubject("Redefinição de senha solicitada");
                                //Conteúdo do e-mail (Com o código para confirmação)
                                mail.setMsg("Olá, " + nomeAux + "\n\nVocê solicitou a alteração da senha. Por favor, informe o código abaixo para confirmar\n\n" + "CÓDIGO: " + getCodigo() + "\n\nCaso não tenha solicitado, recomendamos que você altere sua senha imediatamente!");
                                //Para quem estou enviando o código
                                mail.addTo(senha.getEmail());
                                //Enviando
                                mail.send();

                                //Aqui apenas crio um alert para informar que o e-mail foi enviado com sucesso
                                Alert enviado = new Alert(Alert.AlertType.CONFIRMATION);

                                enviado.setTitle("Redefinição de senha");
                                enviado.setHeaderText("Solicitação bem sucedida!");
                                enviado.setContentText("Verifique seu e-mail e informe o código");
                                enviado.showAndWait();

                                //Aqui peço para o usuário digitar o código enviado ao e-mail dele
                                String codigo = JOptionPane.showInputDialog("Informe o código:");
                                int cod = Integer.parseInt(codigo);

                                //Aqui verifico se o código gerado é o mesmo digitado pelo usuário no qual chegou ao seu e-mail
                                if(cod == getCodigo()){

                                    //Informo que a alteração foi feita com sucesso
                                    Alert verificado = new Alert(Alert.AlertType.CONFIRMATION);
                                    verificado.setTitle("Senha alterada");
                                    verificado.setHeaderText("Sua senha foi alterada com sucesso!");
                                    verificado.setContentText("Faça o login novamente com a nova senha.");
                                    verificado.showAndWait();

                                    //Aqui faço a consulta para atualização da senha com a função UPDATE
                                    String sql = "UPDATE funcionario SET senha = (?) WHERE email = (?)";

                                    PreparedStatement sta = conexao.prepareStatement(sql);

                                    //Insiro a nova senha no BD, alterando a antiga
                                    sta.setString(1, senha.getNSenha());
                                    sta.setString(2, senha.getCapturaEmail());
                                    sta.execute();
                                    sta.close();

                                } else {

                                    //Caso de algo errado, vai cair nesse alert
                                    Alert erro = new Alert(Alert.AlertType.ERROR);
                                    erro.setTitle("Falha na alteração");
                                    erro.setHeaderText("Não foi possível alterar sua senha!");
                                    erro.setContentText("Verifique se o código digitado é o mesmo enviado ao e-mail.");
                                    erro.showAndWait();
                                }

                            } catch (EmailException e1) {
                                e1.printStackTrace();
                            }

                            //Verifico se o campo NOVA SENHA e REPETIR A NOVA senha são iguais
                        } else if(!senha.getNSenha().equalsIgnoreCase(senha.getNSenhaRep())) {
                            Alert erro = new Alert(Alert.AlertType.ERROR);
                            erro.setTitle("Erro ao refedefinir senha");
                            erro.setHeaderText("Nova senha não confere");
                            erro.setContentText("As senhas informadas são diferentes. Por favor, confira as senhas novamentes.");
                            erro.showAndWait();
                        }
                        //E aqui é um else se caso o e-mail informado não conste no BD
                    } else {
                        Alert erro = new Alert(Alert.AlertType.ERROR);
                        erro.setTitle("Erro ao refedefinir senha");
                        erro.setHeaderText("E-mail não encontrado");
                        erro.setContentText("Por favor, verifique se o e-mail digitado está correto");
                        erro.showAndWait();
                    }
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error");
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
