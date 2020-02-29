import java.text.DecimalFormat;
import java.util.Scanner;

/*
 * Safe sequence 1 :
 * processes : 4 
 * available matrix : 10 5 15 2 15
 * Need matrix : 3 0 3 1 2 0 5 3 0 4 2 0 3 1 2 3 0 3 0 1
 * number of trainers : 4
 * 
 */
/*
 * Safe sequence 2 :
 * processes : 3 
 * available matrix : 15 15 15 15 15
 * Need matrix : 2 3 1 0 0 *** 0 1 0 4 1 *** 1 2 0 0 3
 * number of trainers : 3
 * 
 */
/*
 * Safe sequence 3 : 3 1 2
 * processes : 3 
 * available matrix : 15 15 15 15 15
 * Need matrix : 2 2 2 2 2 *** 2 2 2 2 2  *** 1 1 1 1 1
 * number of trainers : 3
 * 
 */
/*
 * Unsafe sequence :
 * processes : 3
 * available matrix : 15 , 15 , 15 , 15 , 15
 * Need matrix : 6 0 3 1 2 0 5 3 0 4 4 0 3 1 2 3 0 3 0 1
 * number of trainers : 
 * 
 */

public class OSProject {

	static int numberOfProcesses;//input max 100
    static int numberOfResources = 5;// GIVEN input limited to 5
    static int openingHours = 15;
    static int numberOfTrainers;
    	static int countSeq=0;
    static int time = 0;
    	
    static int[][] assigned;//allocation 
    static int[][] needed;//max
    static int[][] remaining;//needed-assigned //need
    static int[] available;//available
    static int[] existing;//actual number of resources //alloc_avail
	static boolean[] processStatus;
	static int[] processesSequence;
	static int[] waitingTime;
	static double[] utilization;
	static int[][] maxCopy;
	static boolean[] checkAvailablity;
	static boolean[] check;//check if the process is in the safe sequence
	static DecimalFormat df = new DecimalFormat("###.###");

    public static void main(String[] args) {
    	
    		Scanner sc = new Scanner(System.in);
    		
    		while(true) {
    			System.out.println("Enter number of trainees ((should not exceed 100)) :");
    				numberOfProcesses = sc.nextInt();
    				if(numberOfProcesses<=100)
    					break;		
		}
    		
		
		assigned = new int[numberOfProcesses][numberOfResources];
		needed = new int[numberOfProcesses][numberOfResources];
		remaining = new int[numberOfProcesses][numberOfResources];
		available = new int[numberOfResources];
		existing = new int[numberOfResources];
		processStatus = new boolean[numberOfProcesses];
		processesSequence = new int[numberOfProcesses];
		waitingTime = new int[numberOfProcesses];
		utilization = new double [numberOfResources];
		maxCopy = new int [numberOfProcesses][numberOfResources];
		checkAvailablity = new boolean[numberOfProcesses];
		check = new boolean[numberOfProcesses];
		
		              // 15 15 15 15 15
		System.out.println("Enter number of occurrences of each instrument :");//number of available time slots for each instrument
			for(int r=0 ; r<numberOfResources ; r++) 
				available[r]= sc.nextInt();
		
		
		System.out.println("Needed matrix: ");
			for(int r=0 ; r<numberOfProcesses ; r++) { //max matrix
				 System.out.println("trainee #"+(r+1));
				for(int c=0 ; c<numberOfResources ; c++) {
					switch(c) {
					case 0 : System.out.println("#hours for - piano - :");
					needed[r][c] = sc.nextInt();
					maxCopy[r][c] = needed[r][c];
					break;
					case 1: System.out.println("#hours for - Violin - :");
					needed[r][c] = sc.nextInt();
					maxCopy[r][c] = needed[r][c];
					break; 
					case 2: System.out.println("#hours for - Drums - :");
					needed[r][c] = sc.nextInt();
					maxCopy[r][c] = needed[r][c];
					break; 
					case 3: System.out.println("#hours for - Guitar - :");
					needed[r][c] = sc.nextInt();
					maxCopy[r][c] = needed[r][c];
					break; 
					case 4: System.out.println("#hours for  - Trumpets - :");
					needed[r][c] = sc.nextInt();
					maxCopy[r][c] = needed[r][c];
					break; 
					}
				}
				System.out.println("__ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __");
			}
		System.out.println("Enter number of trainers :");
		numberOfTrainers = sc.nextInt();
			
		System.out.println("The initial assigned matrix is zeros");//Check 
			for(int r=0 ; r<numberOfProcesses ; r++) 
				for(int c=0 ; c<numberOfResources ; c++) 
					assigned[r][c] = 0;
		
		
    		assigneTrainees() ;
    		
    	}
    
    public static void assigneTrainees() {

    	    		
        int timeCounter =0;
        int availableTrainers = numberOfTrainers;
        
        calculateUtilization();
        
	     while(timeCounter<openingHours) {
	    	 	
	    	 	boolean waiting = true;
	    	 	
	    	 	for(int i=0 ;i< numberOfProcesses ;i++) { 
		   	    waiting = false;
	    	 		for( int j=0 ; j<numberOfResources ;j++) {
	    	 				if(maxCopy[i][j]>0) {
	    	 					waiting = true;
	    	 					if(ok(i,j)&&(available[j]>0)&&(availableTrainers>0)) {
		    	 						assigned[i][j]=1;
		    	 						maxCopy[i][j]--;
		    	 						available[j]--;
		    	 						availableTrainers--;	
		    	 						waiting = false;
		    	 						break;
		    	 					
	    	 					}
	    	 				}
	    	 		}
	   	        if(waiting)
	   	        		waitingTime[i]++;
	    	 	}
	    	 	
	    	 	System.out.println("\n-------------------------");

	    	 	System.out.println("Available number of instruments at time ["+(timeCounter+1)+"] is:");
	    	 	for(int i=0 ; i<numberOfResources ;i++)
	    	 		System.out.print(" "+available[i]);
	    	 	
	    	 	System.out.println("\n Assigned matrix:");
	    	 	printM(assigned , numberOfProcesses , numberOfResources);
	    	 	System.out.println("\n Remaining matrix:");
	    	 	printM(maxCopy , numberOfProcesses , numberOfResources);
	    	 	empty();
	    	 	safeSeq();
	    	 	availableTrainers = numberOfTrainers;
	    	 	timeCounter++;
	     }// enf for j
	     
	    boolean flag = true;
	    for(int i=0 ; i<numberOfProcesses; i++)
	    		if(!check[i])
	    			flag = false;
	    
	    if(flag) {
	    	
	    		System.out.println("\n-------------------------");
	    		System.out.print("Safe sequence :");
	    		
	    		for(int i=0 ;i<numberOfProcesses ;i++)
	    			System.out.print(" "+processesSequence[i]);
	    		
	    		System.out.println("");
	    			    		
	    		System.out.println("\n");
	            
	            for(int i=0; i<numberOfResources ;i++)
	            		System.out.println("Resource ["+(i+1)+"] utlization = "+df.format(utilization[i]*100)+"%");
	            
	            System.out.println("\n-------------------------");
	    		System.out.println("Waiting time :");
	    		for(int i=0; i<numberOfProcesses ;i++)
            		System.out.println("Process ["+(i+1)+"] waiting = "+df.format(waitingTime[i]));
            
	        System.out.println("\nAverage waiting time is :"+df.format(calculateAvgWaitingTime()));

	    		
	    }else {
	    		System.out.println("\nThere is no safe sequence!!!!!!!!");
	    }
	    
	    
	    	 	
	    	 	
    }

    
    private static boolean ok(int p, int r) {
    	
    		if(maxCopy[p][r]>0) {	
    			for(int k=0 ; k<numberOfProcesses ;k++) 
    				if(assigned[k][r]==1)
    					return false;
    		return true;
    		}else {
    			return false;
    		}
    }
    
    private static void safeSeq() {
    	    	boolean flag= true;
    	    	
    		for (int i=0 ;i< numberOfProcesses ;i++) {
    			if(!check[i]) {
    				for(int j=0 ; j< numberOfResources ;j++) {
    					if(maxCopy[i][j]>0) {
    						flag=false;
    						break;
    					}
    				}
    				
    			    if(flag) {
    			    		processesSequence[countSeq++]= i+1;
    			    		check[i]=true;
    				}
    			    
    			    flag = true;
    			}
    		}    			
    }
    
    private static void printM(int[][] m, int r, int c) {
    	
    		for (int i=0 ;i<r;i++) {
    			System.out.println("");
    			for(int j=0 ; j<c ;j++)
    				System.out.print(" "+ m[i][j]);
    		}
    }
    
    private static void empty() {
    	
    		for(int i=0 ; i<numberOfProcesses ; i++)
    			for(int j=0 ; j< numberOfResources ; j++)
    				assigned[i][j]=0;
    }
	
	
	private static void calculateUtilization() {
		
		for(int i=0 ; i<numberOfResources ; i++) {
			for(int j=0 ; j<numberOfProcesses ; j++)
				utilization[i] += needed[j][i];
			utilization[i]= utilization[i]/available[i];//Check
		}
				
	}
	
	private static double calculateAvgWaitingTime() {
		double avgWaiting =0;
		
		for(int i=0 ;i<numberOfProcesses; i++)
			avgWaiting += waitingTime[i];
		
		return avgWaiting/numberOfProcesses;
	}
	
}
