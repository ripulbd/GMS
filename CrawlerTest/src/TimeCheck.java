
public class TimeCheck {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String timeDate = "12:56 AM on 02/08/2014";
		String time = timeDate.substring(0, timeDate.indexOf(" "));
		String date = timeDate.substring(timeDate.indexOf("on ") + 3);
		//System.out.println("Time:" + time + ", Date:" + date);
		String hour = time.substring(0, time.indexOf(":"));
		String minute = time.substring(time.indexOf(":") + 1);
		//System.out.println("Hour:" + hour + ", Minute:" + minute);
		String newHour = "", newTime = "";
		boolean am = timeDate.contains("AM");
		if(!am){
			
			newTime = hour + ":" + minute + " " + date;
			if(!hour.contains("12")){
				newHour = "" + (Integer.parseInt(hour) + 12);
				newTime = newHour + ":" + minute + " " + date;
			}
		}else {
			newTime = hour + ":" + minute + " " + date;
		}
		System.out.println("New Time:" + newTime);
	}

}
