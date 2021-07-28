import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

class question{
    public String question_text;
    public String[] answer_txt;
    public int answer_nr;
    public boolean[] answers;

    public question(int answer_nr) {
        this.answer_nr = answer_nr;
        this.answer_txt = new String[6];
        this.answers = new boolean[6];
    }

    public question(question aux){
        this.answer_nr = aux.answer_nr;
        this.question_text = aux.question_text;
        this.answer_txt = new String[aux.answer_nr];
        this.answers = new boolean[aux.answer_nr];
        for (int i = 0; i < aux.answer_nr; i++) {
            this.answers[i]=aux.answers[i];
            this.answer_txt[i] = aux.answer_txt[i];
        }
    }
}

public class Controller implements Initializable {

    private int answer_nr=3;

    private int question_nr=0, currentquestion=1;

    private List<question> DataList;

    @FXML
    private AnchorPane ap;

    @FXML
    private TextField answer_field1, answer_field2, answer_field3, answer_field4, answer_field5, answer_field6, answer_nr_area;
    private TextField[] answerFields;

    @FXML
    private CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6;
    private CheckBox[] checkBoxes;

    @FXML
    private TextArea questionarea;

    @FXML
    private RadioButton oneanswer, multipleanswers;

    @FXML
    private Button add_answer, substract_answer, modify_question;

    @FXML
    private Text current_question_text;

    @FXML
    private void nextquestion(ActionEvent event){
        if(question_nr+1==currentquestion)return;
        if(question_nr==currentquestion){
            questionarea.setText("");
            for (int i = 0; i < 6; i++) {
                checkBoxes[i].setSelected(false);
                answerFields[i].setText("");
            }
            currentquestion++;
            current_question_text.setText("Question number "+String.valueOf(currentquestion)+" of "+String.valueOf(question_nr));
            modify_question.setText("Save question");
            return;
        }
        currentquestion++;
        loadquestion();
    }

    @FXML
    private void prevquestion(ActionEvent event){
        if(currentquestion==1)return;
        currentquestion--;
        modify_question.setText("Edit question");
        loadquestion();
    }

    private void loadquestion(){
        current_question_text.setText("Question number "+String.valueOf(currentquestion)+" of "+String.valueOf(question_nr));
        question aux = new question(DataList.get(currentquestion-1));
        questionarea.setText(aux.question_text);
        if(aux.answer_nr!=1) {
            answer_nr_area.setDisable(false);
            multipleanswers.setSelected(true);
            for (int i = 0; i < aux.answer_nr; i++) {
                checkBoxes[i].setVisible(true);
                checkBoxes[i].setSelected(aux.answers[i]);
                answerFields[i].setVisible(true);
                answerFields[i].setText(aux.answer_txt[i]);
            }
            for (int i = aux.answer_nr; i < 6; i++) {
                checkBoxes[i].setVisible(false);
                checkBoxes[i].setSelected(false);
                answerFields[i].setVisible(false);
                answerFields[i].setText("");
            }
        }
        else{
            answer_nr_area.setDisable(true);
            oneanswer.setSelected(true);
            checkBoxes[0].setVisible(false);
            for (int i = 1; i < 6; i++) {
                checkBoxes[i].setVisible(false);
                answerFields[i].setVisible(false);
            }
            answerFields[0].setText(aux.answer_txt[0]);
        }
        answer_nr= aux.answer_nr;
    }

    @FXML
    private void savequestion(ActionEvent event){

        question aux = new question(answer_nr);
        aux.question_text = questionarea.getText();
        for (int i = 0; i < answer_nr; i++) {
            aux.answers[i] = checkBoxes[i].isSelected();
            aux.answer_txt[i] = answerFields[i].getText();
        }

        if(modify_question.getText().equals("Save question")) {
            DataList.add(aux);

            /*System.out.println(aux.question_text);
            System.out.println(aux.answer_nr);
            for (int i = 0; i < answer_nr; i++) {
                System.out.println(aux.answers[i]+aux.answer_txt[i]);
            }*/

            questionarea.setText("");
            for (int i = 0; i < aux.answer_nr; i++) {
                checkBoxes[i].setSelected(false);
                answerFields[i].setText("");
            }
            question_nr++;
            currentquestion++;
            current_question_text.setText("Question number " + String.valueOf(currentquestion) + " of " + String.valueOf(question_nr));
        }else {
            DataList.set(currentquestion-1,aux);
            System.out.println("merge");
        }
    }

    @FXML
    private void change_answer_nr(ActionEvent event){
        if(event.getSource()==oneanswer) {
            checkBoxes[0].setVisible(false);
            for (int i = 1; i < 6; i++) {
                checkBoxes[i].setVisible(false);
                checkBoxes[i].setSelected(false);
                answerFields[i].setVisible(false);
                answerFields[i].setText("");
            }
            answer_nr_area.setDisable(true);
            answer_nr=1;
            return;
        }
        if(oneanswer.isSelected())return;

        try {answer_nr = Integer.parseInt(answer_nr_area.getText());}
        catch (NumberFormatException error) {
            System.out.println(error);
            answer_nr_area.setText(String.valueOf(answer_nr));
            return;
        }

        if (event.getSource() == answer_nr_area) {
            if (answer_nr > 6) {
                answer_nr_area.setText("6");
                answer_nr = 6;
            }
            if (answer_nr < 2) {
                answer_nr_area.setText("2");
                answer_nr = 2;
            }
        }

        if (event.getSource() == multipleanswers)
            answer_nr_area.setDisable(false);

        if (event.getSource() == add_answer) {
            if (answer_nr < 6) answer_nr++;
            answer_nr_area.setText(String.valueOf(answer_nr));
        }

        if (event.getSource() == substract_answer) {
            if (answer_nr > 2) answer_nr--;
            answer_nr_area.setText(String.valueOf(answer_nr));
        }

        for (int i = 0; i < answer_nr; i++) {
            checkBoxes[i].setVisible(true);
            answerFields[i].setVisible(true);
        }
        for (int i = answer_nr; i < 6; i++) {
            checkBoxes[i].setVisible(false);
            checkBoxes[i].setSelected(false);
            answerFields[i].setVisible(false);
            answerFields[i].setText("");
        }
    }

    @FXML
    private void opentest(ActionEvent event){
        Stage stage = (Stage) ap.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file);
        try {
            Scanner myReader = new Scanner(file);
            String data = myReader.nextLine();

            question_nr=0;
            currentquestion=1;
            DataList = new ArrayList<question>();

            question_nr = Integer.parseInt(data);
            String txt_aux;
            for(int i=0;i<question_nr;i++){
                txt_aux = myReader.nextLine();
                String question_txt_aux = "";
                while(!txt_aux.equals("&&")) {
                    question_txt_aux = question_txt_aux + txt_aux + '\n';
                    txt_aux = myReader.nextLine();
                    System.out.println(question_txt_aux);
                }
                txt_aux = myReader.nextLine();
                int ans_nr = Integer.parseInt(txt_aux);
                System.out.println(ans_nr);
                question aux = new question(ans_nr);
                aux.question_text=question_txt_aux;
                if(ans_nr!=1) {
                    for (int j = 0; j < ans_nr; j++) {
                        txt_aux = myReader.nextLine();
                        aux.answers[j] = Boolean.parseBoolean(txt_aux);
                        txt_aux = myReader.nextLine();
                        aux.answer_txt[j] = txt_aux;
                    }
                }
                else {
                    txt_aux = myReader.nextLine();
                    aux.answer_txt[0] = txt_aux;
                }
                DataList.add(aux);
                txt_aux = myReader.nextLine();
                System.out.println(txt_aux);
            }
            currentquestion=question_nr;
            current_question_text.setText("Question number "+String.valueOf(currentquestion)+" of "+String.valueOf(question_nr));
            modify_question.setText("Edit question");
            questionarea.setText("");
            loadquestion();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    @FXML
    private void savetest(ActionEvent event){
        try{
            File file = new File("Save.txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            PrintWriter writer = new PrintWriter("Save.txt", "UTF-8");
            writer.println(question_nr);
            for(int i=0;i<question_nr;i++) {
                question aux = new question(DataList.get(i));
                writer.println(aux.question_text);
                writer.println("&&");
                writer.println(aux.answer_nr);
                if (aux.answer_nr != 1) {
                    for (int j = 0; j < aux.answer_nr; j++) {
                        writer.println(aux.answers[j]);
                        writer.println(aux.answer_txt[j]);
                    }
                }
                else writer.println(aux.answer_txt[0]);
                writer.println("/");
            }
            writer.close();
        }
        catch (IOException e){
            System.out.println(e);
        }

    }

    @FXML
    private void close(ActionEvent event){
        Platform.exit();
    }

    /*scene.setOnKeyPressed(e -> {
        if (e.getCode() == KeyCode.A) {
            System.out.println("A key was pressed");
        }
    });*/

    @FXML
    private void keyboard_ctrl(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            savetest(null);
        }else if(event.getCode().equals(KeyCode.ESCAPE)){
            close(null);
        }
        System.out.println(event.getCode());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataList = new ArrayList<question>();
        ToggleGroup group = new ToggleGroup();
        oneanswer.setToggleGroup(group);
        multipleanswers.setToggleGroup(group);

        //Am creat un vector de checkboxuri si de textfield pentru a fi mai usor sa lucram cu ele mai tarziu
        checkBoxes = new CheckBox[6];
        checkBoxes[0]=checkbox1;
        checkBoxes[1]=checkbox2;
        checkBoxes[2]=checkbox3;
        checkBoxes[3]=checkbox4;
        checkBoxes[4]=checkbox5;
        checkBoxes[5]=checkbox6;

        answerFields = new TextField[6];
        answerFields[0]=answer_field1;
        answerFields[1]=answer_field2;
        answerFields[2]=answer_field3;
        answerFields[3]=answer_field4;
        answerFields[4]=answer_field5;
        answerFields[5]=answer_field6;
    }
}
