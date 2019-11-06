import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int tamArreglo=20;
        Scanner sc=new Scanner(System.in);
        System.out.println("Ingrese cantidad de hilos para dividir la tarea");
        int k=sc.nextInt();
        Random rnd=new Random();
        Task[] cantTasks=new Task[k];
        Future<Integer>[] resultadosTasks=new Future[k];
        int[] arrNum=new int[tamArreglo];
        for (int i=0;i<arrNum.length;i++){
            arrNum[i]=rnd.nextInt(10)+1;
        }
        System.out.println(Arrays.toString(arrNum));
        ExecutorService executor=Executors.newFixedThreadPool(k);
        int indiceInf=0;
        int cantCadaHilo=arrNum.length/k;
        int indiceSup=cantCadaHilo-1;
        //hago el for hasta k-1 asi al ultimo hilo le asigno lo que quede (por problemas de redondeo)

        //en este for creo las Tasks, asignando distintas regiones del arreglo a cada una.
        for(int i=0;i<k-1;i++){
            Task task=new Task(indiceInf,indiceSup,arrNum);
            indiceInf+=cantCadaHilo;
            indiceSup+=cantCadaHilo;
            cantTasks[i]=task;
        }
        indiceSup=arrNum.length-1;
        Task task=new Task(indiceInf,indiceSup,arrNum);
        cantTasks[k-1]=task;

        //en este for lleno el arreglo con instancias de Future, donde cada una contendra el resultado de la Task
        for(int i=0;i<cantTasks.length;i++){
            Future<Integer> future=executor.submit(cantTasks[i]);
            resultadosTasks[i]=future;
        }
        int sumaArr=0;

        //en este for recorro ese arreglo de instancias Future para obtener la suma de cada una.
        for(int i=0;i<resultadosTasks.length;i++){
            sumaArr+=resultadosTasks[i].get();
        }
        executor.shutdown();
        System.out.println(sumaArr);





    }
}
