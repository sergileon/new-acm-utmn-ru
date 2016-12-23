package ru.sibint.olymp.checker;

public class CheckingInfo {
	
	private CheckingResult verdict;
	private int testNumber;
	private Double time = 0.00;
	private Long memory = 0L;
	private String message;
	
	public void setVerdict(CheckingResult newVerdict) {
		verdict = newVerdict;
	}
	
	public CheckingResult getCheckingResult() {
		return verdict;
	}
	
	public void setTestNumber(int newTestNumber) {
		testNumber = newTestNumber;
	}
	
	public void setMessage(String newMessage) {
		message = newMessage;
	}
	
	public int getTestNumber() {
		return testNumber;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setTime(Double newTime) {
		time = newTime;
	}
	
	public Double getTime() {
		return time;
	}
	
	public void setMemory(long newMemory) {
		memory = newMemory;
	}
	
	public long getMemory() {
		return memory;
	}	
	
	@Override
	public String toString() {
		return "Verdict: " + verdict.toString() + ", Time: " + time + ", Test: " + testNumber + ", Memory: " + memory;
	}
	
}
