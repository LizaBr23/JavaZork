package StartPackage;

public class GenericClass<Recipe>{
    private Recipe value;

    public GenericClass(Recipe  value) {
        this.value = value;
    }

    public Recipe getValue() {
        return value;
    }
    public void setValue(Recipe value) {
        this.value = value;
    }

}
