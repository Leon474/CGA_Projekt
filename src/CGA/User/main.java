package CGA.User;
/**
 * Created by Fabian on 16.09.2017.
 */
import CGA.User.Game.Game;
import org.joml.Matrix3f;
import org.joml.Vector3f;


public class main {
    public static void main(String[] args) {
        //Window
        Game game = new Game(1289, 720, false, false, "Rush Hour", 3, 3);
        game.run();

        /*
        Vector3f a = new Vector3f();

        Vector3f b = new Vector3f();
        Vector3f akreuz=new Vector3f();
        float zwischen;
        float zwischenzwei;
        double ergeb;
        double ergeb2;
        double ergebnis;
        double cosAlpha;
        double RADcosAlpha;
        double Alpha;













        Vector3f v0=new Vector3f();
        Vector3f v1=new Vector3f();

        //1.1
        //---------------Normaliesieren---------------------------
        zwischen = (float) Math.sqrt(2);
        a = new Vector3f(2.0f, -3.0f, -6.0f);
        b = new Vector3f(zwischen, -3, 5);

        ergeb=Math.sqrt((Math.pow(2,2)+Math.pow(-3,2)+Math.pow(-6,2)));

        //--------------Ausgabe----------------------------
        System.out.println("-----------------------Aufgabe 1.1: v0---------------------");
        System.out.println("|vo|= "+ ergeb);
        System.out.println("v0= "+a.normalize());
        System.out.println("-----------------------------------------------------------");

        //-------------------------------------------------


        ergeb=Math.sqrt((Math.pow(zwischen,2)+Math.pow(-3,2)+Math.pow(-6,2)));
        System.out.println("-----------------------Aufgabe 1.1: v1---------------------");
        System.out.println("|v1|= "+ ergeb);
        System.out.println("v0= "+b.normalize());
        System.out.println("-----------------------------------------------------------");


        //1.2
        //---------------Skalarprodukt-------------------------------
        System.out.println("-----------------------Aufgabe 1.2---------------------");
        zwischen = (float) Math.sqrt(8);
        zwischenzwei = (float) Math.sqrt(3);

        a = new Vector3f(-2, 2, zwischen);
        b = new Vector3f(3, 2, zwischenzwei);

        ergeb = a.dot(b);
        System.out.println("v0*v1= "+ergeb);

        ergebnis=Math.sqrt((Math.pow(2,2)+Math.pow(2,2)+Math.pow(zwischen,2)));     //|a|
        ergeb2=Math.sqrt((Math.pow(3,2)+Math.pow(2,2)+Math.pow(zwischenzwei,2)));   //|b|
        cosAlpha=ergeb/(ergebnis*ergeb2);
        RADcosAlpha=ergeb/(ergebnis*ergeb2);


        //--------------Ausgabe----------------------------
        System.out.println("|a|= "+ergebnis);
        System.out.println("|b|= "+ergeb2);
        System.out.println("cos= "+cosAlpha);
        System.out.println("Rad= "+Math.acos(cosAlpha));

        //System.out.println(Math.cos(90));//Es wird Automatisch in Randian gerechnet. Frage: warum rehnet die Methode "toRandians()" in Grad um?

        RADcosAlpha=Math.toDegrees(RADcosAlpha);
        System.out.println("Grad= "+Math.acos(RADcosAlpha));
        System.out.println("-----------------------------------------------------------");


        //1.3
        //--------------Kreutzprodukt--------------------------------
        System.out.println("-----------------------Aufgabe 1.3---------------------");

        a = new Vector3f(1, -1, 3);
        b = new Vector3f(1, 1, 0);

        //v0*v1
        //[1,-1,3]*[a,a,0]=0 =a und a müssen gleich sein um sich aufzuheben und 0 um gleich 0 zu werden(Orthogonal)

        a.cross(b,akreuz);

        //--------------Ausgabe----------------------------
        System.out.println("Kreutzprodukt: "+ akreuz);
        System.out.println("-----------------------------------------------------------");

        //1.4
        // --------------Trigonometrie----------------------------
        System.out.println("-----------------------Aufgabe 1.4---------------------");
        double Sb=40;
        double Sc=50;
        double Sa=0;
        double Zerg;
        double CosZerg;
        double Beta;

        Sa=Math.sqrt(Math.pow(Sc,2)-Math.pow(Sb,2));
        System.out.println("a: "+ Sa);

        Zerg=Sb/Sc;
        CosZerg=Math.toDegrees(Math.acos(Zerg));
        System.out.println("Math.cos("+Zerg+")= "+CosZerg);


        Beta= 180-90-CosZerg;
        System.out.println("Beta: "+ Beta);

        System.out.println("-----------------------------------------------------------");

        //1.5
        // ---------------------Matritzenrechnung-----------------------------------
        System.out.println("-----------------------Aufgabe 1.5---------------------");

        Alpha=0.00;
        Alpha=Math.toRadians(Alpha);
        double CosAlpha=Math.cos(Alpha);
        double SinAlpha=Math.sin(Alpha);
        double MinSinAlpha=-(Math.sin(Alpha));
        Vector3f Vek=new Vector3f(4,0,0);
        Vector3f erg=new Vector3f();
        Matrix3f M=new Matrix3f();

        M= new Matrix3f((float)CosAlpha,(float)SinAlpha,0.0f,(float)MinSinAlpha,(float)CosAlpha,0,0,0,1);
        erg=Vek.mul(M);
        //--------------Ausgabe----------------------------
        System.out.println(M);
        System.out.println(erg);
        System.out.println("-----------------------------------------------------------");

        //1.6
        //-----------------------Matritzenrechnung2-----------------------------------
        System.out.println("-----------------------Aufgabe 1.6---------------------");

        int i=0;
        int x=0;
        String bustabe="";
        Vector3f O=new Vector3f();
        Vector3f A= new Vector3f(2,2,0);
        Vector3f B= new Vector3f(4,2,0);
        Vector3f C= new Vector3f(4,4,0);
        Vector3f D= new Vector3f(2,4,0);
        Matrix3f U=new Matrix3f();
        Matrix3f M1=new Matrix3f((float)0.5,0,0,0,(float)0.5,0,0,0,1);
        Matrix3f M2=new Matrix3f(2,0,0,0,2,0,0,0,1);
        Matrix3f M3=new Matrix3f(3,0,0,0,1,0,0,0,1);


        for (x=0;x<3;x++){

            if (x==0){
                U=M1;
                System.out.println("------------------Matrix1----------------");
            }
            else if (x==1){
                U=M2;
                System.out.println("------------------Matrix2----------------");
            }
            else if (x==2){
                U=M3;
                System.out.println("------------------Matrix3----------------");
            }

            for (i=0;i<4;i++){
                if (i==0){
                    O=A;
                    bustabe="a= ";
                }
                else if(i==1){
                    O=B;
                    bustabe="b= ";
                }
                else if (i==2){
                    O=C;
                    bustabe="c= ";
                }
                else if (i==3)
                {
                    O=D;
                    bustabe="d= ";
                }
                O.mul(U,erg);
                System.out.println(bustabe+erg);



            }
            O=new Vector3f();
            U=new Matrix3f();
            erg=new Vector3f();

        }*/
    }




}
