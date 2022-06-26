import java.util.Objects;

public class Pipe {

    boolean isFilled;
    boolean bottom;
    boolean top;
    boolean left;
    boolean right;


    /**
     *
     * @param isFilled
     * @param bottom
     * @param top
     * @param left
     * @param right
     */
    public Pipe(boolean isFilled, boolean bottom, boolean top, boolean left, boolean right) {
        this.isFilled = isFilled;
        this.bottom = bottom;
        this.top = top;
        this.left = left;
        this.right = right;
    }

    public void rotate(boolean clockwise){
        if (clockwise) {
            boolean aux = top;
            top = left;
            left = bottom;
            bottom = right;
            right = aux;
        } else {
            boolean aux = top;
            top = right;
            right = bottom;
            bottom = left;
            left = aux;
        }
    }

    @Override
    public String toString() {

        if (top && left && bottom && right){
            if (isFilled)
                return "[-|-]";
            return  " -|- ";
        }
        if (top && left && bottom){
            if (isFilled)
                return "[-| ]";
            return  " -|  ";
        }
        if (top && bottom && right){
            if (isFilled)
                return "[ |-]";
            return  "  |- ";
        }
        if (top && left && right){
            if (isFilled)
                return "[-'-]";
            return  " -'- ";
        }
        if (left && bottom && right){
            if (isFilled)
                return "[-,-]";
            return  " -,- ";
        }
        if (top && left){
            if (isFilled)
                return "[-' ]";
            return  " -'  ";
        }
        if (top && right){
            if (isFilled)
                return "[ '-]";
            return  "  '- ";
        }
        if (top && bottom){
            if (isFilled)
                return "[ | ]";
            return  "  |  ";
        }
        if (left && bottom){
            if (isFilled)
                return "[-, ]";
            return  " -,  ";
        }
        if (bottom && right){
            if (isFilled)
                return "[ ,-]";
            return  "  ,- ";
        }
        if (left && right){
            if (isFilled)
                return "[- -]";
            return  " - - ";
        }
        if (left){
            if (isFilled)
                return "[-  ]";
            return  " -   ";
        }
        if (right){
            if (isFilled)
                return "[  -]";
            return  "   - ";
        }
        if (bottom){
            if (isFilled)
                return "[ , ]";
            return  "  ,  ";
        }
        if (top){
            if (isFilled)
                return "[ ' ]";
            return  "  '  ";
        }
        return " x ";
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pipe pipe = (Pipe) o;
        return isFilled == pipe.isFilled &&
                bottom == pipe.bottom &&
                top == pipe.top &&
                left == pipe.left &&
                right == pipe.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isFilled, bottom, top, left, right);
    }
}
