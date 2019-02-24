public class Tests
{

    static double[] pleasework = {1,2,3,4};

    public static void main(String[] args)
    {
        pleasework=testfunction();
        for(int i=0; i<pleasework.length; i++)
        {
            System.out.print(pleasework[i] + " ");
        }
    }

    private static double[] testfunction()
    {
        double[] changevalue={4,3,2,1};
        return changevalue;
    }
}