import java.util.concurrent.Callable;

public class Task implements Callable<Integer> {
    private int indiceInf,indiceSup,resultado;
    private int[] arr;
    public Task(int i,int j,int[] arreglo){
        indiceInf=i;
        indiceSup=j;
        resultado=0;
        arr=arreglo;
    }

    public Integer call() throws Exception{
        for (int k=indiceInf;k<=indiceSup;k++){
            resultado+=arr[k];
        }
        return resultado;

    }
}
