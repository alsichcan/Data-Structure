public class substring implements Comparable<substring> {

    private String key; // substring이 입력 데이터에서의 위치
    private String value; // substring의 String 값
    private int lineNum; // substring이 입력 데이터에서 몇번째 line에 있는지
    private int index; // substring이 특정 line에서 몇번째에 해당하는 substring인지

    //////////////////////////////////////////////////////////////////////////////////////////

    public substring(String input){
        value = input;
    }

    public substring(String input, int lineNum, int index) {
        this.lineNum = lineNum;
        this.index = index;
        key = "(" + lineNum + ", " + index + ")";
        value = input;
    }
    //////////////////////////////////////////////////////////////////////////////////////////
    public String getKey(){
        return this.key;
    }

    public String getValue(){
        return this.value;
    }

    public boolean inPattern(substring other, int length){
        // 두 substring이 동일한 부분에서 나온 패턴인지 확인하는 메소드
        // this는 패턴의 시작 substring, other는 패턴의 마지막 substring
        if(this.lineNum == other.lineNum) {
            // 두 substring이 같은 줄에서 나온 것이어야한다.
            if (this.index + (length - 6) == other.index) {
                // 두 substring이 패턴의 양 끝을 이루어야한다.
                return true;
            }
        }
        return false;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public int compareTo(substring other) {
        // substring 객체의 비교는 substring의 String 값을 기준으로 비교한다.
        return this.value.compareTo(other.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        substring other = (substring) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        // 해시코드는 String값의 각 자리수의 아스키코드 값의 합을 100으로 나눈 나머지
        int hash = 0;
        for (int i = 0; i < value.length(); i++) {
            char letter = value.charAt(i);
            int iletter = (int) letter;
            hash += iletter;
        } // end for
        return (hash % 100);
    }

}

