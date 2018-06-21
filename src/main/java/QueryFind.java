import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class QueryFind {
    private String level;
    private String kind;
    private List<String> subjects;
    private int hours;
    private String means;

    public QueryFind() {
    }

    public QueryFind(String level, String kind, List<String> subjects, int hours, String means) {
        this.level = level;
        this.kind = kind;
        this.subjects = subjects;
        this.hours = hours;
        this.means = means;
    }

    public String getLevel() {
        return level;
    }

    public String getKind() {
        return kind;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public int getHours() {
        return hours;
    }

    public String getMeans() {
        return means;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("hours >= " + hours);

        if (!level.isEmpty()) {
            sql.append(" AND level = '").append(level).append('\'');
        }

        if (!kind.isEmpty()) {
            sql.append(" AND kind = '").append(kind).append('\'');
        }

        if (!subjects.isEmpty()) {
            sql.append(" AND (subject = '").append(subjects.get(0)).append('\'');

            for (int i = 1; i < subjects.size(); ++i) {
                sql.append(" OR subject = '").append(subjects.get(i)).append('\'');
            }

            sql.append(")");
        }

        if (!means.isEmpty()) {
            sql.append(" AND means = '").append(means).append('\'');
        }

        return sql.toString();
    }
}
