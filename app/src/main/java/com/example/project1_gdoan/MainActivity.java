package com.example.project1_gdoan;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView[] textViews = new TextView[30];
    private Button[] buttons = new Button[26];
    private Button buttonEnter, buttonDel;
    private int rowNum = 1;
    private int[] buttonColors = new int[26];
    private Words words = new Words();
    private String secretWord = words.get_word();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int buttonId;

        for (int i = 0; i <= 25; i++) {
            char letter = (char) ('A' + i);
            buttonId = getResources().getIdentifier("button" + letter, "id", getPackageName());
            buttons[i] = findViewById(buttonId);
        }

        for (int i = 0; i < 30; i++){
            int resID = getResources().getIdentifier("textView" + (i + 1), "id", getPackageName());
            textViews[i] = findViewById(resID);
        }

        for (int i = 0; i < 26; i++) {
            buttons[i].setBackgroundColor(Color.BLACK); // Set default color
            buttonColors[i] = Color.BLACK;
        }

        buttonEnter = findViewById(R.id.enter_button);
        buttonDel = findViewById(R.id.buttonDel);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String buttonText = button.getText().toString();
                char letter = buttonText.charAt(0);

                if (rowNum >= 1 && rowNum <= 6) {
                    int startIndex = (rowNum - 1) * 5;
                    int endIndex = rowNum * 5;

                    boolean rowFilled = true;

                    for (int i = startIndex; i < endIndex; i++) {
                        if (textViews[i].getText().toString().equals("")) {
                            textViews[i].setText(buttonText);
                            rowFilled = false;
                            break; // Exit the loop after setting the first empty TextView
                        }
                    }

                    if (rowFilled) {
                        Toast.makeText(MainActivity.this, "Row is already filled.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle invalid rowNum
                    Toast.makeText(MainActivity.this, "Invalid row selection.", Toast.LENGTH_SHORT).show();
                }

            }

        };
        for (int a = 0; a < 26; a++) {
            buttons[a].setOnClickListener(buttonClickListener);
        }

        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rowNum >= 1 && rowNum <= 6) {
                    int startIndex = (rowNum - 1) * 5;
                    int endIndex = rowNum * 5;

                    for (int i = endIndex - 1; i >= startIndex; i--) {
                        if (!textViews[i].getText().toString().equals("")) {
                            textViews[i].setText("");
                            break;
                        }
                    }
            }
        }
    });

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rowNum >= 1 && rowNum <= 6) {
                    int startIndex = (rowNum - 1) * 5;
                    int endIndex = rowNum * 5;

                    boolean currentRowFilled = true;

                    for (int i = startIndex; i < endIndex; i++) {
                        if (textViews[i].getText().toString().equals("")) {
                            currentRowFilled = false;
                            break;
                        }
                    }

                    if (currentRowFilled) {
                        String rowText = "";

                        for (int i = 0; i < secretWord.length(); i++) {
                            String currentChar = String.valueOf(secretWord.charAt(i));
                            String rowChar = textViews[startIndex + i].getText().toString();

                            if (rowChar.equals(currentChar)) {
                                textViews[startIndex + i].setTextColor(Color.GREEN);
                                buttons[currentChar.charAt(0) - 'A'].setBackgroundColor(Color.GREEN);
                                buttonColors[currentChar.charAt(0) - 'A'] = Color.GREEN; // Save button color
                            } else if (secretWord.contains(rowChar)) {
                                textViews[startIndex + i].setTextColor(Color.rgb(255, 165, 0)); // Orange Color
                                buttons[rowChar.charAt(0) - 'A'].setBackgroundColor(Color.rgb(255, 165, 0)); // Orange Color
                                buttonColors[rowChar.charAt(0) - 'A'] = Color.rgb(255, 165, 0); // Save button color
                            } else {
                                buttons[rowChar.charAt(0) - 'A'].setBackgroundColor(Color.RED);
                                buttonColors[rowChar.charAt(0) - 'A'] = Color.RED; // Save button color
                            }

                            rowText += rowChar;
                        }

                        if (rowText.equals(secretWord)) {
                            showWinDialog();
                        }

                        if (rowNum < 6) {
                            rowNum++;
                        } else if (!rowText.equals(secretWord)) {
                            showLoseDialog(secretWord);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Fill the current row first.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle invalid rowNum
                    Toast.makeText(MainActivity.this, "Invalid row selection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Display the Win Dialog
    private void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Congratulations! You've found the secret word. \nPlay again?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetGame();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish(); // Close the app
                    }
                });
        builder.create().show();
    }

    // Display the Lose Dialog
    private void showLoseDialog(String secretWord) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You've lost. The secret word was: " + secretWord + ". \nPlay again?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetGame();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish(); // Close the app
                    }
                });
        builder.create().show();
    }

    private void resetGame() {
        // Generate a new secret word
        secretWord = words.get_word();

        // Reset everything to default
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setText("");
            textViews[i].setTextColor(Color.BLACK);
        }

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(true);
        }
        resetButtonColors();
        rowNum = 1;
    }


    private void resetButtonColors() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackgroundColor(Color.BLACK);
            buttonColors[i] = Color.BLACK; // Reset button color in the array
        }
    }

}