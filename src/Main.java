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
		System.out.println("Ingrese cantidad de tareas en las que se dividira la suma de los elementos");
		int k = sc.nextInt();
		System.out.println("Ingrese cantidad de hilos (2,3,4)");
		int h = sc.nextInt();
		Random rnd = new Random();
		int[] arrNum = new int[tamArreglo];
		for (int i = 0; i < arrNum.length; i++) {
			arrNum[i] = rnd.nextInt(10) + 1;
		}
		ExecutorService executor = Executors.newFixedThreadPool(h);
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

		// listaFuture contendra todas las instancias Future q se generaran por ejecutar
		// las tasks
		java.util.List<Future<Integer>> listaFuture = executor.invokeAll(listaTasks);

		int sumaArr = 0;

		// en este for recorro la lista instancias Future para obtener la suma de
		// cada una.
		for (int i = 0; i < listaFuture.size(); i++) {
			sumaArr += listaFuture.get(i).get();
		}
		executor.shutdown();
		System.out.println("La suma de los elementos del arreglo es: " + sumaArr);

	}
}
