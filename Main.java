package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import static java.lang.Integer.parseInt;
import static javafx.application.Platform.exit;

public class Main extends Application {
    private TextArea s2C = new TextArea();
    private TextArea score = new TextArea();
    private Button exit = new Button();
    private Button again = new Button();
    private Label firstWord = new Label("Waiting...");
    private Label secondWord = new Label("Waiting...");
    private Label thirdWord = new Label("Waiting...");
    private TextField firstAnswer = new TextField();
    private TextField secondAnswer = new TextField();
    private TextField thirdAnswer = new TextField();
    private HBox root2;
    private VBox root;
    NetworkConnection conn = createClient();

    private Parent myClientGui(){
        TextField input = new TextField();
        TextField input2 = new TextField();
        Button enter = new Button();

        input.setPromptText("Enter Port number here");
        input2.setPromptText("Enter IP address here");

        s2C.setDisable(true);
        score.setDisable(true);


        enter.setText("Connect");
        enter.setOnAction(event -> {
            conn.setPort(input.getText());
            conn.setIP(input2.getText());
            clientTime();
        });

        firstAnswer.setMaxHeight(25);
        firstAnswer.setMaxWidth(150);
        secondAnswer.setMaxHeight(25);
        secondAnswer.setMaxWidth(150);
        thirdAnswer.setMaxHeight(25);
        thirdAnswer.setMaxWidth(150);

        firstAnswer.setOnAction(event -> conn.checkAnswer(firstAnswer));
        secondAnswer.setOnAction(event -> conn.checkAnswer(secondAnswer));
        thirdAnswer.setOnAction(event -> conn.checkAnswer(thirdAnswer));

        secondAnswer.setDisable(true);
        thirdAnswer.setDisable(true);

        firstAnswer.setPromptText("Enter answer here");
        secondAnswer.setPromptText("Enter answer here");
        thirdAnswer.setPromptText("Enter answer here");

        conn.setAnswers(firstAnswer, secondAnswer, thirdAnswer);

        BorderPane b = new BorderPane();
        b.setPadding(new Insets(20));


        root = new VBox(20,input, input2, enter, firstWord, firstAnswer, secondWord, secondAnswer, thirdWord, thirdAnswer);
        score.appendText("Scoreboard: \nYou:0\nOpponent:0");
        root.getChildren().add(s2C);

        b.setBottom(root2);
        //root2.setPrefSize(100,150);
        b.setCenter(root);
        root.setPrefSize(600,350); //600 by 350
        exit.setText("Quit");
        again.setText("Play Again");

        exit.setOnAction(event -> {
            conn.send("quit");
            root.getChildren().remove(exit);
            root.getChildren().remove(again);
            this.stop();
            exit();
        });
        again.setOnAction(event -> {
            conn.send("again");
            root.getChildren().remove(exit);
            root.getChildren().remove(again);
            conn.opWins = 0;
            conn.wins = 0;
            conn.changeBoard(score);
            s2C.setDisable(false);
            score.setDisable(false);

        });
        return b;
    }



    @Override
    public void start(Stage primaryStage){
        primaryStage.setScene(new Scene(myClientGui()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void stop(){
        try {
            System.out.println("Called stop");
            conn.stop();
        }
        catch (Exception e){
            System.out.println("Close failed");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getStackTrace());

        }

    }

    public myClient createClient(){

        return new myClient(data -> {
            Platform.runLater(() -> {
                if(data.toString().intern() == "end"){
                    root.getChildren().add(exit);
                    root.getChildren().add(again);
                    return;
                }
                if(data.toString().intern() == "wait"){
                    s2C.appendText("\nWaiting for another client to join...");
                    return;
                }
                if(data.toString().intern() == "start"){
                    s2C.appendText("\n2 Clients ready play. Start Game\n");
                    s2C.setDisable(false);
                    score.setDisable(false);
                    return;
                }
                if(data.toString().intern() == "win"){
                    ++conn.wins;
                    conn.changeBoard(score);
                    return;
                }
                if(data.toString().intern() == "lose"){
                    ++conn.opWins;
                    conn.changeBoard(score);
                    return;

                }
                conn.setInfo(data.toString());
                s2C.appendText(data.toString() + "\n");

            });
        });
    }


    public void clientTime(){
        try
        {
            conn.setConnection();
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage() + "\n");
            System.out.println(e.getMessage() + "\n");
            System.out.println(e.getCause());
        }

    }
}

abstract class NetworkConnection{

    public String currInfo, ONE, TWO, THREE;
    public Consumer<Serializable> callback;
    public connect myConnection;
    public Serializable recentMove;
    HashMap<TextField,String> cheatSheet;
    TextField answer1;
    TextField answer2;
    TextField answer3;
    ArrayList<TextField> order;
    boolean on;
    int wins,opWins;

    public NetworkConnection(Consumer<Serializable> data){
        this.callback = data;
        wins = 0;
        opWins = 0;
        order = new ArrayList<>();
        cheatSheet = new HashMap<>();
        ONE = "cat";
        TWO = "dog";
        THREE = "mhm";

    }
    public void setConnection() throws Exception {

        Socket mysocket = new Socket(getIP(),getPort());
        System.out.println(getIP() + ": " + getPort());
        myConnection = new connect(mysocket);
        on = true;
        myConnection.start();

    }
    public void setInfo(String data){ currInfo = data; }

    public void setAnswers(TextField ans1, TextField ans2, TextField ans3){
        this.answer1 = ans1;
        this.answer2 = ans2;
        this.answer3 = ans3;

        order.add(answer1);
        order.add(answer2);
        order.add(answer3);

        cheatSheet.put(answer1,ONE);
        cheatSheet.put(answer2,TWO);
        cheatSheet.put(answer3,THREE);
    }

    public void checkAnswer(TextField answer){
        String ans = answer.getText();

        if(ans.equals(cheatSheet.get(answer))){
            for (TextField t : order){
                if(t.isDisable()){
                    t.setDisable(false);
                    break;
                }
            }

        }

    }
    abstract protected void setPort(String port);
    abstract protected int getPort();
    abstract protected String getIP();
    abstract protected void setIP(String ip);

    public void stop(){
        try {
            send("e");
            on = false;
            myConnection.connSocket.close();
        }
        catch (Exception e){}

    }
    public void changeBoard(TextArea score){
        score.clear();
        score.appendText("Scoreboard: \nYou:" + this.wins + "\nOpponent:" + this.opWins);
    }

    public void sendMove(Serializable data){
        this.recentMove = data;

        try{myConnection.out.writeObject(data);}
        catch (Exception e){System.out.println("Send exception.");}//access output stream and write to it
    }

    public void send(Serializable data){
        try{myConnection.out.writeObject(data);}
        catch (Exception e){System.out.println("Send exception.");}//access output stream and write to it
    }

    class connect extends Thread{
        //out and socket here for closing and accessing output stream
        private ObjectOutputStream out;
        private ObjectInputStream in;
        Socket connSocket;

        connect(Socket s){
            this.connSocket = s;

        }

        public void run() {

            try (
                    ObjectOutputStream out = new ObjectOutputStream(connSocket.getOutputStream());
                    ObjectInputStream  in = new ObjectInputStream(connSocket.getInputStream());
            ) {

                this.out = out;
                this.in = in;
                while (on) {
                    Serializable data = (Serializable) in.readObject();
                    callback.accept(data);
                }
            }

            catch(Exception e){
                System.out.println("Bad connection ");
                System.out.println(e.getLocalizedMessage() + "\n");
                System.out.println(e.getMessage() + "\n");
                System.out.println(e.getCause());
            }

        }
    }
}


class myClient extends NetworkConnection{
    private int port;
    private String IP;

    myClient(Consumer<Serializable> data){
        super(data);
    }

    protected void setPort(String port) {
        this.port = parseInt(port);
    }

    protected int getPort(){
        return port;
    }

    protected String getIP(){return IP;}
    protected void setIP(String IP){this.IP = IP;}

}
