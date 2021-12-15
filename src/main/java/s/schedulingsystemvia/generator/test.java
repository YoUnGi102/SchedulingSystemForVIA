package s.schedulingsystemvia.generator;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class test {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<String> input = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            input.add(sc.nextLine());
        }

        for (int j = 0; j < input.size(); j++) {
            System.out.println("\tcase " + j + " -> Color.valueOf("+input.get(j)+")");
        }

    }

}
