package am.aca.quiz.software;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        sessionFactory.openSession();
        menu();
    }

    private static void menu(){

        Scanner in=new Scanner(System.in);
        System.out.println("Choose User or Admin");
        String authentication=in.nextLine();
        while (true){
            switch (authentication.toLowerCase()){
                case "user" :
                    System.out.println("Opening Login Page Enter your Email and Password ");
                    System.out.println("stex authentication and authorisation  kgna @ ete sax lava ancnuma myus ej");
                    System.out.println("*************************************************************");
                    System.out.println("For Category Press c" +
                            "\n For History Press h" +
                            "\n For Profile press p" +
                            "\n To LogOut press o");
                    String button =in.nextLine();
                    switch (button) {
                        case "c":
                            System.out.println("Choose Category");
                            String tab = in.nextLine();
                            switch (tab.toLowerCase()) {
                                case "programming":
                                    System.out.println("Choose Language");
                                    String language = in.nextLine();
                                    switch (language.toLowerCase()) {
                                        case "java":
                                            System.out.println("Choose Test name");
                                            String testName = in.nextLine();

                                            switch (testName.toLowerCase()) {
                                                case "test1":
                                                    break;
                                                case "test2":
                                                    break;
                                             }
                                             break;
                                        case "c#":
                                            break;
                                    }
                                    break;
                                case "networking":
                                    break;
                                case "DataBases":
                                    break;
                            }
                            break;
                    }
                    break;
                case "admin" :
                    break;
            }
        }

    }
}
