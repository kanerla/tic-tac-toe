import java.util.Scanner;

/**
 * TicTacToe program simulates a game of Tic Tac Toe against a computer.
 *
 * @author Laura Kanerva
 * @version 20181212
 */

public class TicTacToe {
    /**
    * Row number given by user or computer.
    */
    public static int givenRow;

    /**
    * Column number given by user or computer.
    */
    public static int givenColumn;

    /**
    * Amount of rows in the gameboard.
    */
    public static int amountOfRows = 0;

    /**
    * Amount of columns in the gameboard.
    */
    public static int amountOfColumns = 0;

    /**
    * Amount of marks required to win the game.
    */
    public static int requiredToWin = 0;

    /**
    * Counting how many moves have been made in the game.
    */
    public static int counter = 0;

    /**
    * Player's character.
    */
    public static char player = 'X';

    /**
    * Computer's character.
    */
    public static char computer = 'O';

    /**
    * Determines whether game has ended or not.
    */
    public static boolean playing = true;

    /**
    * Scanner for reading user input.
    */
    public static Scanner reader = new Scanner(System.in);

    public static Board board;
    public static Computer ai;

    /**
   * This is the main method which makes use of selectMeasurements, createBoard,
   * printBoard and Play methods.
   *
   * @param args Unused.
   */
    public static void main(String[] args) {
        System.out.println();
        System.out.println(" ------------------------- ");
        System.out.println("  Welcome to Tic Tac Toe!  ");
        System.out.println(" ------------------------- ");
        selectMeasurements();
        board = new Board(amountOfRows, amountOfColumns, requiredToWin);
        ai = new Computer(amountOfRows, amountOfColumns);     
        board.createBoard();
        board.printBoard();
        Play();
    }

    /**
    * This method is for asking the user to select the size of the gameboard and amount of marks required
    * to win. No invalid digits, letters or special characters will be accepted.
    * For example, user selecting -5 as the height or the letter H as the amount of winning marks would lead
    * to an error message.
    * Method demands measurements until user input is valid.
    */
    public static void selectMeasurements() {
        while (amountOfRows < 3) {
            System.out.println("Please select the height of the board:");
            try {
                amountOfRows = Integer.parseInt(reader.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again.");
                continue;
            }
            if (amountOfRows < 3) {
                System.out.println("Minimum height is 3.");
            }
        }
        while (amountOfColumns < 3) {
            System.out.println("Please select the width of the board:");
            try {
                amountOfColumns = Integer.parseInt(reader.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again.");
                continue;
            }
            if (amountOfColumns < 3) {
                System.out.println("Minimum width is 3.");
            }
        }
        while (requiredToWin < 3 || requiredToWin > 5 || requiredToWin > amountOfRows || requiredToWin > amountOfColumns) {
            System.out.println("Please select how many marks are required to win:");
            try {
                requiredToWin = Integer.parseInt(reader.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again.");
                continue;
            }
            if (requiredToWin < 3) {
                System.out.println("Minimum is 3.");
            } else if (requiredToWin > 5) {
                System.out.println("Sorry, maximum is 5.");
            } else if (requiredToWin > amountOfRows || requiredToWin > amountOfColumns) {
                System.out.println("Sorry, required marks can't exceed the shortest side of the gameboard.");
            }
        }
    }

    /**
    * Starting the gameplay.
    *
    * Method starts the game and keeps it going until someone wins the game or there are no empty spaces left on the board.
    * Turns are selected based on the counter. When it's dividable by two, it's computer's turn.
    * Other times it's player's turn.
    * Method prints the board at the end of every turn and checks if anyone won with their last move.
    */
    public static void Play() {
        while (playing) {
            counter ++;
            givenRow = -1;
            givenColumn = -1;
            System.out.println();
            if (counter % 2 != 0) {
                playersTurn();
            } else {
                computersTurn();
            }
            board.printBoard();
            checkForWin();
        }  
    }

    /**
    * Getting player's move.
    *
    * Method is asking player to place their mark on the board, until it's valid. Error message will be
    * sent if input is invalid. Example of an invalid input would be numbers outside the board or places
    * that are already occupied.
    */
    public static void playersTurn() {
        while (!validPlacement(givenRow, givenColumn)) {
            System.out.println("Please enter row & column:");
            try {
                givenRow = Integer.parseInt(reader.nextLine().trim()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again.");
                continue;
            }
            try {
                givenColumn = Integer.parseInt(reader.nextLine().trim()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again.");
                continue;
            }
            if (validPlacement(givenRow, givenColumn)) {
                board.insertChar(givenRow, givenColumn, player);
                break;
            } else {
                System.out.println("Can't play that!");
            }  
        }
    }

    /**
    * Getting computer's move.
    *
    * Method inserts computer's move on the board when it is valid. Invalid move would be anything
    * outside the gameboard or an already occupied space.
    */
    public static void computersTurn() {
        System.out.println("Computer's turn:");
        while (!validPlacement(givenRow, givenColumn)) {
            givenRow = ai.getRow();
            givenColumn = ai.getColumn();
            if (validPlacement(givenRow, givenColumn)) {
                board.insertChar(givenRow, givenColumn, computer);
                break;
            }  
        }
    }

    /**
    * Checking for win or a draw.
    *
    * Method calls horizontalWin, verticalWin and diagonalWin methods to see if either player or computer
    * has reached enough marks in a row to win the game. If so, the game ends.
    * If all slots are filled and no one won, the game ends with a draw.
    */
    public static void checkForWin() {
        if(board.horizontalWin() == player || board.horizontalWin() == computer) {
            GameOver();
        } else if (board.verticalWin() == player || board.verticalWin() == computer) {
            GameOver();
        } else if (board.diagonalWin() == player || board.diagonalWin() == computer) {
            GameOver();
        } else if (counter == (amountOfRows * amountOfColumns)) {
            playing = false;
            System.out.println();
            System.out.println("Game over! It's a tie.");
        }
    }

    /**
    * Returns true or false based on whether the placement is valid or not.
    *
    * Method returns true, if the coordinates given in parameters are inside the grid and empty.
    * Otherwise method returns false.
    *
    * @param r Integer to mark the row.
    * @param c Integer to mark the column
    * @return true if coordinates are valid and playable, false if not.
    */
    public static boolean validPlacement(int r, int c) {
        if (r >= amountOfRows || r < 0) {
            return false;
        }
        if (c >= amountOfColumns || c < 0) {
            return false;
        }
        if (board.getChar(r, c) == 'X' || board.getChar(r, c) == 'O') {
            return false;
        }
        return true;
    }

    /**
    * Ending the game.
    *
    * Method ends the game and displays the winner based on who's turn it was when the winning row was detected.
    */
    public static void GameOver() {
        playing = false;
        System.out.println();
        if (counter % 2 != 0) {
            System.out.println("Congratulations! You won the game.");
        } else {
            System.out.println("Game over! Computer wins the game.");
        }
    }
}

/**
 * Board class contains methods for maintaining the board.
 *
 * The class contains methods for creating and printing the gameboard, inserting and retrieving a
 * character from the board, and checking for horizontal, vertical and diagonal winning streaks on the gameboard.
 */
class Board {
    private char[][] board;
    private int col;
    private int row;
    private int amountToWin;

    /**
    * Board class constructor.
    *
    * The constructor is called when a board object is created. It gets the amount of rows, columns and marks required for a win
    * in parameters.
    * @param r Integer to mark the amount of rows.
    * @param c Integer to mark the amount of columns.
    * @param win the amount of marks required for a win.
    */
    public Board(int r, int c, int win) {
        this.board = new char[r][c];
        this.row = r;
        this.col = c;
        this.amountToWin = win;
    }

    /**
    * Creating the gameboard.
    *
    * Method goes through the 2 dimensional array and places an underscore to mark an empty space in each index.
    */
    public void createBoard() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = '_';
            }
        }
    }

    /**
    * Printing the gameboard.
    *
    * Method goes through the 2 dimensional array and prints the gameboard.
    * Each slot has been divided with a "|" sign.
    */
    public void printBoard() {
        for (int i = 0; i < row; i++) {
            System.out.println();
            for (int j = 0; j < col; j++) {
                if (j == 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " | ");
            }
        }
        System.out.println();
    }

    /**
    * Inserts character to given coordinates.
    *
    * Method inserts the character to the board in the coordinates given in parameters.
    *
    * @param row Integer to mark the row.
    * @param col Integer to mark the column.
    * @param c char to mark the character.
    */
    public void insertChar(int row, int col, char c) {
        board[row][col] = c;
    }

    /**
    * Returns character found in given coordinates.
    *
    * Method returns the character found in the coordinates given in parameters.
    *
    * @param r Integer to mark the row.
    * @param c Integer to mark the column.
    * @return character on the board in given coordinates.
    */
    public char getChar(int r, int c) {
        return board[r][c];
    }

    /**
    * Returns winning character.
    *
    * Method goes through the board to see if either player or computer has enough marks in a horizontal row to win the game.
    * Method then returns the character that wins.
    * If neither player or computer has enough marks, method returns an underscore (empty character).
    *
    * @return character that has enough marks in a horizontal row to win the game, or empty.
    */
    public char horizontalWin() {
        int counter = 0;
        char empty = '_';
        char previous = ' ';
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (board[i][j] != empty) {
                    if (board[i][j] == previous) {
                        counter ++;
                    } else {
                        counter = 1;
                        previous = board[i][j];
                    }
                } else {
                    counter = 0;
                    previous = empty;
                }
                if (counter == amountToWin) {
                    return previous;
                }         
            }
            previous = empty;
            counter = 0;
        }
        return empty;
    }

    /**
    * Returns winning character.
    *
    * Method goes through the board to see if either player or computer has enough marks in a vertical row to win the game.
    * Method then returns the character that wins.
    * If neither player or computer has enough marks, method returns an underscore (empty character).
    *
    * @return character that has enough marks in a vertical row to win the game, or empty.
    */
    public char verticalWin() {
        int counter = 0;
        char empty = '_';
        char previous = ' ';
        for (int j = 0; j < col; j++){
            for (int i = 0; i < row; i++){
                if (board[i][j] != empty) {
                    if (board[i][j] == previous) {
                        counter ++;
                    } else {
                        counter = 1;
                        previous = board[i][j];
                    }
                } else {
                    counter = 0;
                    previous = empty;
                }
                if (counter == amountToWin) {
                    return previous;
                }         
            }
            previous = empty;
            counter = 0;
        }
        return empty;
    }

    /**
    * Returns winning character.
    *
    * Method goes through the board to see if either player or computer has enough marks in an ascending or descending diagonal row to win the game.
    * Method then returns the character that wins.
    * If neither player or computer has enough marks, method returns an underscore (empty character).
    *
    * @return character that has enough marks in a diagonal row to win the game, or empty.
    */
    public char diagonalWin() {
        int counter = 0;
        char empty = '_';
        char previous = ' ';
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (board[i][j] != empty) {
                    previous = board[i][j];
                    counter = 1;
                    for (int k = 1; k <= amountToWin && i + k < row && j + k < col; k++) {
                        if (board[i+k][j+k] == previous) {
                            counter++;
                            if(counter == amountToWin) {
                                return previous;
                            }
                        } else {
                            break;
                        }
                    }
                    counter = 1;
                    previous = board[i][j];
                    for (int k = 1; k <= amountToWin && i + k < row && j - k >= 0; k++) {
                        if (board[i+k][j-k] == previous) {
                            counter++;
                            if(counter == amountToWin) {
                                return previous;
                            }
                        } else {
                            break;
                        }
                    }
                }      
            }
            previous = empty;
            counter = 0;
        }
        return empty;
    }
}

/**
 * Computer class contains methods for maintaining computer AI.
 *
 * The class contains methods for creating a Computer object and retrieving row and column numbers.
 */
class Computer {
    private int columns;
    private int rows;

    /**
    * Computer class constructor.
    *
    * The constructor is called when a computer object is created. It gets the amount of rows and columns
    * on the gameboard in parameters.
    * @param r Integer to mark the amount of rows.
    * @param c Integer to mark the amount of columns.
    */
    public Computer(int r, int c) {
        this.rows = r;
        this.columns = c;
    }

    /**
    * Returns an Integer marking the row.
    *
    * Method creates a random number between 0 and the amount of rows on the gameboard.
    * Method then returns the number.
    *
    * @return random Integer between 0 and the amount of rows on the gameboard.
    */
    public int getRow() {
        int x = (int)(Math.random() * rows);
        return x;
    }

    /**
    * Returns an Integer marking the column.
    *
    * Method creates a random number between 0 and the amount of columns on the gameboard.
    * Method then returns the number.
    *
    * @return random Integer between 0 and the amount of columns on the gameboard.
    */
    public int getColumn() {
        int x = (int)(Math.random() * columns);
        return x;
    }
}