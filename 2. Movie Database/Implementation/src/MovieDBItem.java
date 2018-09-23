
/******************************************************************************
 * MovieDB의 인터페이스에서 공통으로 사용하는 클래스.
 */
public class MovieDBItem implements Comparable<MovieDBItem> {

    private final String genre;
    private final String title;

    public MovieDBItem(String genre, String title) {
        if (genre == null) throw new NullPointerException("genre");
        if (title == null) throw new NullPointerException("title");

        this.genre = genre;
        this.title = title;
    }

    public String getGenre() { return genre;}

    public String getTitle() { return title;}


    // 두 개의 MovieDBItem를 비교하여 순서에 관한 정보를 제공하는 Integer 값을 반환한다.
    // 리턴값이 양수이면 this가 other보다 순서가 앞이라는 뜻이고
    // 리턴값이 0이면 this가 other과 같다는 의미이며
    // 리턴값이 음수이면 this가 other보다 순서가 뒤라는 뜻이다.
    // 장르를 사전순으로 우선적으로 정렬하고, 그 후 같은 장르에 대해서 제목의 사전순으로 정렬한다.
    // compareTo()메소드의 반환 값의 범위는 char 자료형의 범위 안에 있다.
    // 따라서 장르와 제목의 두 기준의 우선순위를 위해 genre의 compareTo() 값에 대해 char의 최대치보다 큰 상수(10000)을 곱한다.
    // 이 때문에 title에 대한 compareTo()값과 상관없이 genre에 대한 compareTo()값이 결과를 좌우하며,
    // genre에 대한 compareTo()값이 0일 때에만 (즉, this와 other의 장르가 동일한 경우에만) title에 대한 compareTo()값이 의미가 있다.
    @Override
    public int compareTo(MovieDBItem other) {
        int answer = this.getGenre().compareTo(other.getGenre()) * 10000 + this.getTitle().compareTo(other.getTitle());
        return answer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MovieDBItem other = (MovieDBItem) obj;
        if (genre == null) {
            if (other.genre != null)
                return false;
        } else if (!genre.equals(other.genre))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

}
