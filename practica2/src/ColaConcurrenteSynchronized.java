package src;

/*  Ejercicio 4: ColaConcurrente que utiliza la clase Nodo.
 *  Implementa una cola concurrente utilizando synchronized.
 */

public class ColaConcurrenteSynchronized {
    private Nodo head;
    private Nodo tail;

    public ColaConcurrenteSynchronized() {
        this.head  = new Nodo("hnull");
        this.tail  = new Nodo("tnull");
        this.head.next = this.tail;
    }

    // Aplicamos synchronized para la exclusión mutua
    public synchronized Boolean enq(String x) {
        Nodo newnode = new Nodo(x);
        if(this.head.next == this.tail) {
            newnode.next = this.tail;
            this.head.next = newnode;	
        } else {
            Nodo last =  this.tail.next;
            newnode.next = this.tail;
            last.next = newnode;
        }
        this.tail.next = newnode;
        return true;
    }

    // Al aplicar synchornized a otro método, esto causará que
    // ningún hilo phaga deq() mientras otro hace enq(), y viceversa.
    public synchronized String deq() {
        if(this.head.next == this.tail) {
            return "empty";
        }
        Nodo first = this.head.next;
        this.head.next = first.next;		
        return first.item;
    }

    
    public synchronized void print() {
        System.out.println("Print ");
        Nodo pred = this.head;
        Nodo curr = pred.next;
        System.out.println(pred.item);
        while (curr.item != "tnull") {
          pred = curr;
          curr = curr.next;
          System.out.println(pred.item);
        }
    }
}