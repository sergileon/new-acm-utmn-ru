package ru.sibint.olymp.api;

public class CheckingInfo {
	
	private CheckingResult verdict;
	private int testNumber;
	private long time;
	private long memory;
	
	public void setVerdict(CheckingResult newVerdict) {
		verdict = newVerdict;
	}
	
	public CheckingResult getCheckingResult() {
		return verdict;
	}
	
	public void setTestNumber(int newTestNumber) {
		testNumber = newTestNumber;
	}
	
	public int getTestNumber() {
		return testNumber;
	}
	
	public void setTime(long newTime) {
		time = newTime;
	}
	
	public long getTime() {
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
