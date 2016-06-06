package wolfusz.voidlauncher.essentials;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class Label {
    private CharSequence label;
    private String regex;
    private Integer firstAppPos;
//	private Integer lastAppPos;
//	private Integer iterator;

    public CharSequence getText() { return this.label; }
    public String getRegex() { return this.regex; }
    public Integer getFirstApp() { return this.firstAppPos; }
    //	public Integer getLastApp() { return this.lastAppPos; }
    public Integer getPos() { return this.firstAppPos; } //+this.iterator; }

    public void setRegex(String regex) { this.regex = regex; }
    public void setFirstApp(int position) { this.firstAppPos = position; }
//	public void setLastApp(int position) { this.lastAppPos = position; }

/*	public int nextPos() {

		 * first = 3
		 * last = 5
		 * iterator = 0
		 * iterator = (0+1)%(5-3+1) = 1%3 = 1
		 * iterator = (1+1)%(5-3+1) = 2%3 = 2
		 * iterator = (2+1)%(5-3+1) = 3%3 = 0

		this.iterator = (this.iterator+1)%(this.lastAppPos-this.firstAppPos+1);
		return this.firstAppPos + this.iterator;
	}*/

    public Label(String label, String regex, int firstAppPos) { //, int lastAppPos) {
        this.label = label;
        this.regex = regex;
        this.firstAppPos = firstAppPos;
//		this.lastAppPos = lastAppPos;
//		this.iterator = 0;
    }

    public Label(String label, String regex) { this(label, regex, -1); } //, -1); }
    public Label(String label) { this(label, ""); }

    @Override
    public String toString() {
        return this.getText().toString();
    }
}
