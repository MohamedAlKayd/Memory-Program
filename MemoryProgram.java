/*
Mohamed Mahmoud
Memory Program
 */


/*
 - Start of the Program -
 */

import java.util.LinkedList;

public class Memory {

	/*
	class StringInterval
	 */

	public class StringInterval{
		int id;
		int start;
		int length;

		/*
		constructor
		 */

		public StringInterval(int id,int start,int length){
			this.id = id;
			this.start = start;
			this.length = length;

		}

	}

	/*
	--------Memory Fields--------------
	 */

	public LinkedList<StringInterval> intervalList = new LinkedList<StringInterval>();

	public char[] memoryArray;

	static int idCount;

	/*
	--------Memory Methods----------------
	 */

	/*
	 Constructor
	 */

	Memory(int length){
		memoryArray = new char[length];
		idCount = 0;
	}

	/*
	 Put Method
	 Get the Parameters for StringInterval Type
	 @param Success: boolean to know if s was added to the list, default is true
	 @param Gap: boolean to know if there is at least one gap
	 @param GapBigEnough: boolean to know if at least one gap is big enough for s
	 */

	public int put(String s){
		boolean success = true;
		boolean gap = false;
		boolean gapBigEnough = false;

		int id = idCount;
		int start = 0;

		int gapLength;
		StringInterval prevElement=null;

		/*
		if list is empty
		 */

		if (intervalList.size() == 0){
			start = 0;
		}

		/*
		list is not empty and gap at 0
		@pre: there is a gap at beginning of the list/array
		size of gap
		 */

		else if  (intervalList.getFirst().start != 0){
			gapLength = intervalList.getFirst().start;
			if (gapLength>=s.length()){
				start = 0;
				gap = true;
				gapBigEnough = true;
			}
		}

		/*
		list is not empty
		loops around the elements to find gap (the case of the last element is treated separately)
		gap located
		gap is big enough
		element placed before s
		loop breaks because we found a gap big enough
		 */

		else{
			for (StringInterval element : intervalList){
				if (element != intervalList.getLast()){
					if (next(intervalList,element) != element.start + element.length){
						gap = true;
						gapLength = next(intervalList,element)-(element.start + element.length) ;
						if (gapLength>=s.length()){
							start = element.start + element.length;
							gap = true;
							gapBigEnough = true;
							prevElement = element;
							break;
						}
					}
				}
			}

			/*
			looks if place at the end if there was no gap big enough
			the end of the list has a gap big enough
			*/
			
			if(memoryArray.length-(intervalList.getLast().start+intervalList.getLast().length) >= s.length() && !gapBigEnough){
				start = intervalList.getLast().start+intervalList.getLast().length;
				gapBigEnough = true;
				prevElement = intervalList.getLast();
			}
		}

		/*
		there are gaps
		no gap big enough
		list gaps are gone
		put at the end
		still not enough place after defragmentation
		 */

		if (gap){
			if (!gapBigEnough){
				defragment();
				start = intervalList.getLast().start + intervalList.getLast().length;
				if (memoryArray.length - start < s.length()){
					success = false;
				}
			}
		}

		/*
		there was no gap
		list is not empty
		 */

		else {
			if (intervalList.size()!=0){
				start = intervalList.getLast().start + intervalList.getLast().length;
			}
			if (memoryArray.length - start < s.length()){
				success = false;
			}

		}

		/*
		s can be put in the list
		declare StringInterval str
		place the string s in intervallist
		*/

		if (success){
			StringInterval str = new StringInterval(id,start,s.length());
			if (intervalList.size()==0){
				intervalList.addFirst(str);
			}

			/*
			added next to the previous element (of the gap)
			added at the end
			 */

			else{
				if (gapBigEnough){
					intervalList.add(intervalList.indexOf(prevElement)+1,str);
				}else{
					intervalList.add(str);
				}
			}

			/*
			place the string s in available place in memoryArray
			*/

			for (int j = 0; j<s.length();j++){
				memoryArray[start] = s.charAt(j);
				start = ++start;

			}
			idCount = ++idCount;

			/*
			successfully put into memory
			*/

			return (id);
		}else{
			return -1;
		}

	}

	/*
	Get Methods
	 */

	public String get(int id){
		String word=null;

		/*
		loops around the intervallist
		id found
		word is no longer null
		 */

		for (StringInterval element : intervalList) {
			if (element.id == id) {
				word = new String();
				int start = element.start;
				int length = element.length;
				for (int i = start; i < start + length; i++) {
					word += memoryArray[i];
				}
			}
		}
		return word;
	}

	/*
	loops around elements in interval list
	found an object that whose start field in the array is return the same first char of s
	checks if the object has the same length as s
	checks if each char is the same
	this is true when all the characters are checked
	 */

	public int get(String s){
		int id = -1;
		boolean found = false;
		for(StringInterval element : intervalList){
			if (!found){
				if (memoryArray[element.start]==s.charAt(0)){
					if (element.length == s.length()){
						for (int i = 0; i<s.length();i++){
							if (s.charAt(i)==memoryArray[element.start+i]){
								if(i == s.length()-1) {
									id = element.id;
									found = true;
								}
							}
						}
					}
				}
			}
		}
		return id;
	}

	/*
	Remove Methods
	 */

	public String remove(int id) {
		String word = new String();
		boolean present = false;
		StringInterval elementRemoved = null;
		for (StringInterval element : intervalList) {
			if (element.id == id) {
				elementRemoved = element;
				for (int i = element.start; i < (element.length + element.start); i++) {
					word += String.valueOf(memoryArray[i]);

				}
				present = true;
				break;
			}else{
				continue;
			}
		}
		if (present){
			intervalList.remove(elementRemoved);
			return word;

		}
		else{
			return null;
		}
	}

	/*
	loops around elements in interval list
	will go through this as long as it is not found
	found an object that whose start field in the array is return the same first char of s
	checks if the object has the same length as s
	checks if each char is the same
	this is true when all the characters are checked
	the string id found
	 */

	public int remove(String s){
		int id = -1;
		boolean found = false;
		int index = 0;

		for(StringInterval element : intervalList){
			if (!found){
				if (memoryArray[element.start]==s.charAt(0)){
					if (element.length == s.length()){
						for (int i = 0; i<s.length();i++){
							if (s.charAt(i)==memoryArray[element.start+i]){
								if(i == s.length()-1) {
									id = element.id;

									found = true;

								}
							}
						}
					}
				}
			}
			if(found){break;}
			++index;
		}

		if (found){
			intervalList.remove(index);
		}
		return id;
	}

	/*
	Defragment method
	Gap at the beginning
	Backward shift
	Elements are shifted (by the gap length) in the memory array
	Loops again in the memory array
	Consider the elements that are after the gap
	Change the start field of the elements shifted
	Loops around the element of the list except the last element
	Gap located
	Backward shift
	Elements are shifted (by the gap length) in the memory array
	Consider the elements that are after the gap
	Change the start field of the elements shifted
	 */

	public void defragment() {
		int gapLength = 0;
		if (intervalList.getFirst().start !=0){
			gapLength = intervalList.getFirst().start;
			for (int i = 0; i < memoryArray.length - gapLength; i++){
				memoryArray[i] = memoryArray[i + gapLength];
			}

			for (StringInterval otherElement : intervalList) {
				if (otherElement.start > 0) {
					otherElement.start -=gapLength;
				}
			}
		}
			for (StringInterval element : intervalList) {
				if (element != intervalList.getLast()) {
					if (next(intervalList, element) != element.start + element.length) {
						gapLength = next(intervalList, element) - (element.start + element.length);
						for (int i = element.start + element.length; i < memoryArray.length - gapLength; i++) {
							memoryArray[i] = memoryArray[i + gapLength];
						}
						for (StringInterval otherElement : intervalList) {
							if (otherElement.start > element.start) {
								otherElement.start -=gapLength;
							}
						}
					}
				}
			}
	}

	/*
	------------ Helper -------------
	This method returns the start field of the consecutive element of an element
	 */

	public int next(LinkedList<StringInterval> intervalList,StringInterval element){
		int index = intervalList.indexOf(element);
		StringInterval nextElement = intervalList.get(index+1);


		int start = nextElement.start;
		return start;

	}

	/*
	------------ Testing -------------
	 */

	public static void main(String args[]){
		Memory memory = new Memory(33);
		System.out.println(memory.put("aaa"));
		System.out.println(memory.put("aaaa"));
		System.out.println(memory.put("a"));
		System.out.println(memory.put("aaaaa"));
		System.out.println(memory.put("aaaaaa"));
		System.out.println(memory.remove("aaa"));
		System.out.println(memory.memoryArray);

	}
}

/*
 - End of the Program -
 */