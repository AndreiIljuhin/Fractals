# Fractals
launch from Apache Maven

mvn exec:java -Dexec.mainClass="com.fractal.Mandelbrot"

you can specify your x and y coordinates and size:

mvn exec:java -Dexec.mainClass="com.fractal.Mandelbrot" -Dexec.args="-1.88488933694469 0.00000000081387 0.00000000000024"

or

java -cp fractal-0.0.1-SNAPSHOT.jar com.fractal.Mandelbrot


Used some code from

https://introcs.cs.princeton.edu/java/stdlib/Picture.java.html
https://introcs.cs.princeton.edu/java/32class/Complex.java.html
https://introcs.cs.princeton.edu/java/32class/Mandelbrot.java.html
