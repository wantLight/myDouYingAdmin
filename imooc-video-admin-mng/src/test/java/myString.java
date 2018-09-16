import java.util.Scanner;

/**
 * Created by xyzzg on 2018/9/6.
 */
public class myString {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        //String n = sc.next();
        String str=sc.nextLine();
        char []c =str.toCharArray();
        for (int i=0;i<c.length;i++) {
            if (c[i] >= '0' && c[i] <= '9'){
                System.out.print(11111);
                c[i] = '1';
            }
            else if (c[i] >= 'a' && c[i] <= 'z'){
                c[i] = '0';
            }
        }
        System.out.print(c);
    }
}
