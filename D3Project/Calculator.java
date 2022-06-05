import java.util.LinkedList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator implements ActionListener {
  JFrame frame;
  JPanel panel;
  JTextField textField;
  JButton[] numberButtons = new JButton[10];
  JButton[] functionButtons = new JButton[18];
  String[] functionLabels = {"SIN", "COS", "TAN", "RAD", "SQRT", "LOG", "LN", "/", "*", "-", "+", ".", "(-)", "=", ")", "2ND", "DEL", "CLR"};

  LinkedList<String> nums = new LinkedList<String>();
  LinkedList<String> operations = new LinkedList<String>();
  boolean second = false;
  boolean radians = false;

  public Calculator () {
    // Create frame
    frame = new JFrame("Calculator");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Calculator");
    frame.setSize(470, 550);
    frame.setLayout(null);

    //create textfield
    textField = new JTextField();
    textField.setBounds(50, 25, 350, 50);
    textField.setEditable(false);

    //create numbers
    for (int i = 0; i < 10; i++) {
      if (i < 9) {
        numberButtons[i] = new JButton(String.valueOf(i + 1));
      } else {
        numberButtons[i] = new JButton("0");
      }
      numberButtons[i].addActionListener(this);
      numberButtons[i].setFocusable(false);
    }

    //create functions
    for (int i = 0; i < functionButtons.length; i++) {
      functionButtons[i] = new JButton(functionLabels[i]);
      functionButtons[i].addActionListener(this);
      functionButtons[i].setFocusable(false);
    }

    //create panel for buttons
    panel = new JPanel();
    panel.setBounds(50, 100, 350, 400);
    panel.setLayout(new GridLayout(8, 4, 10, 10));

    //add buttons
    panel.add(functionButtons[0]);
    panel.add(functionButtons[1]);
    panel.add(functionButtons[2]);
    panel.add(functionButtons[3]);
    panel.add(functionButtons[4]);
    panel.add(functionButtons[5]);
    panel.add(functionButtons[6]);
    panel.add(functionButtons[7]);
    panel.add(numberButtons[0]);
    panel.add(numberButtons[1]);
    panel.add(numberButtons[2]);
    panel.add(functionButtons[8]);
    panel.add(numberButtons[3]);
    panel.add(numberButtons[4]);
    panel.add(numberButtons[5]);
    panel.add(functionButtons[9]);
    panel.add(numberButtons[6]);
    panel.add(numberButtons[7]);
    panel.add(numberButtons[8]);
    panel.add(functionButtons[10]);
    panel.add(numberButtons[9]);
    panel.add(functionButtons[11]);
    panel.add(functionButtons[12]);
    panel.add(functionButtons[13]);
    panel.add(functionButtons[14]);
    panel.add(functionButtons[15]);
    panel.add(functionButtons[16]);
    panel.add(functionButtons[17]);

    frame.add(panel);
    frame.add(textField);

    frame.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    // Number buttons (0-9)
    for (int i = 0; i < 10; i++) {
      if (e.getSource() == numberButtons[i]) {
        if(nums.size() != operations.size()){
          int j = nums.size() - 1;
          String appendText = nums.get(j).concat(numberButtons[i].getText());
          nums.set(j, appendText);
        } else {
          String appendText = numberButtons[i].getText();
          nums.add(appendText);
        }
      }
    }

    // Radian toggle
    if (e.getSource() == functionButtons[3]) {
      if (radians == false) {
        radians = true;
        functionButtons[3].setText("DEG");
      } else {
        radians = false;
        functionButtons[3].setText("RAD");
      }
    }

    // Basic functions (+, -, *, /)
    for (int i = 7; i < 11; i++) {
      if (e.getSource() == functionButtons[i]) {
        if (nums.size() != operations.size()) {
          String appendText = functionLabels[i];
          operations.add(appendText);
        }
      }
    }

    // Period
    if (e.getSource() == functionButtons[11]) {
      if (nums.size() != operations.size()) {
        int j = nums.size() - 1;
        String appendText = nums.get(j).concat(".");
        nums.set(j, appendText);
      } else {
        nums.add("0.");
      }
    }

    // Negative
    if (e.getSource() == functionButtons[12]) {
      if (nums.size() == operations.size()) {
        nums.add("-");
      }
    }

    // Equals
    if (e.getSource() == functionButtons[13]) {
      evaluate(0, operations.size());
    }

    // Parenthesis

    // 2nd
    if (e.getSource() == functionButtons[15]) {
      if (second == true) {
        second = false;
        functionButtons[0].setText("SIN");
        functionButtons[1].setText("COS");
        functionButtons[2].setText("TAN");
        functionButtons[4].setText("SQRT");
        functionButtons[5].setText("LOG");
        functionButtons[6].setText("LN");
        functionButtons[14].setText("(");

      } else {
        second = true;
        functionButtons[0].setText("SIN^-1");
        functionButtons[1].setText("COS^-1");
        functionButtons[2].setText("TAN^-1");
        functionButtons[4].setText("^");
        functionButtons[5].setText("10^");
        functionButtons[6].setText("e^");
        functionButtons[14].setText(")");
      }
    }

    // DEL

    // CLR
    if (e.getSource() == functionButtons[17]) {
      while (nums.size() > 0) {
        nums.remove();
      }
      while (operations.size() > 0) {
        operations.remove();
      }
    }

    System.out.print(nums);
    System.out.println(operations);
    print();
  }

  public void evaluate(int start, int end) {
    int i;

    //eval multiplication & division
    i = 0;
    while (i != -1) {
      i = linearSearch("*", start, end);
      int placeholder = linearSearch("/", start, end);
      boolean divide = false;
      float output;

      //decides whether multiplication of division occurs 1st
      if (((i > placeholder) && (placeholder != -1)) | (i == -1)) {
        i = placeholder;
        divide = true;
      }

      //carries out operations and deletes xtra array spaces
      if (i != -1) {
        float num1 = Float.valueOf(nums.get(i));
        float num2 = Float.valueOf(nums.get(i + 1));

        if (divide == false) {
          output = num1 * num2;
        } else {
          output = num1 / num2;
        }

        nums.set(i, String.valueOf(output));
        nums.remove(i + 1);
        operations.remove(i);
        end--;
      }
    }

    //eval addition & subtraction
    i = 0;
    while (i != -1) {
      i = linearSearch("+", start, end);
      int placeholder = linearSearch("-", start, end);
      boolean subtract = false;
      float output;

      if (((i > placeholder) && (placeholder != -1)) | (i == -1)) {
        i = placeholder;
        subtract = true;
      }

      if (i != -1) {
        float num1 = Float.valueOf(nums.get(i));
        float num2 = Float.valueOf(nums.get(i + 1));

        if (subtract == false) {
          output = num1 + num2;
        } else {
          output = num1 - num2;
        }

        nums.set(i, String.valueOf(output));
        nums.remove(i + 1);
        operations.remove(i);
        end--;
      }
    }
  }

  public void print() {
    String equation = "";
    for (int i = 0; i < nums.size(); i++) {
      String appendText = equation.concat(nums.get(i));
      equation = appendText;
      try {
        appendText = equation.concat(operations.get(i));
        equation = appendText;
      } catch (Exception e) {
        // nothing here
      }
    }
    textField.setText(equation);
  }


  public int linearSearch(String toFind, int start, int end){
    for (int i = start; i < end; i++) {
      if (toFind.compareToIgnoreCase(operations.get(i)) == 0) {
        return i;
      }
    }
    return -1;
  }

  public int getEndParen(int start, int end){
    for (int i = start; i < end; i++) {
      if (")" == nums.get(i)) {
        return i;
      }
    }
    return -1;
  }

}
