import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Main {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		int tamArreglo = 50000;
		Scanner sc = new Scanner(System.in);
		System.out.println("Ingrese cantidad de hilos para dividir la tarea");
		int k = sc.nextInt();
		Random rnd = new Random();
		int[] arrNum = new int[tamArreglo];
		for (int i = 0; i < arrNum.length; i++) {
			arrNum[i] = rnd.nextInt(10) + 1;
		}
		ExecutorService executor = Executors.newFixedThreadPool(k);
		int indiceInf = 0;
		int cantCadaHilo = arrNum.length / k;
		int indiceSup = cantCadaHilo - 1;
		// hago el for hasta k-1 asi al ultimo hilo le asigno lo que quede (por
		// problemas de redondeo)
		LinkedList<Task> listaTasks = new LinkedList<Task>();
		// en este for creo las Tasks, asignando distintas regiones del arreglo a cada
		// una.
		for (int i = 0; i < k - 1; i++) {
			Task task = new Task(indiceInf, indiceSup, arrNum);
			indiceInf += cantCadaHilo;
			indiceSup += cantCadaHilo;
			listaTasks.add(task);
		}
		indiceSup = arrNum.length - 1;
		Task task = new Task(indiceInf, indiceSup, arrNum);
		listaTasks.add(task);

		// en este for lleno el arreglo con instancias de Future, donde cada una
		// contendra el resultado de la Task
		java.util.List<Future<Integer>> listaFuture = executor.invokeAll(listaTasks);

		int sumaArr = 0;

		// en este for recorro ese arreglo de instancias Future para obtener la suma de
		// cada una.
		for (int i = 0; i < listaFuture.size(); i++) {
			sumaArr += listaFuture.get(i).get();
		}
		executor.shutdown();
		System.out.println(sumaArr);

	}
}
