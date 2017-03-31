package fr.genetic.server.simulation;

import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarDefinition {

    public static final float MOTOR_SPEED = 20F;

    private static final float CHASSIS_MIN_AXIS = 0.1F;

    private static final float CHASSIS_MAX_AXIS = 1.1F;
    private static final float CHASSIS_MIN_DENSITY = 30F;

    private static final float CHASSIS_MAX_DENSITY = 300F;
    private static final float WHEEL_MAX_RADIUS = 0.5F;
    private static final float WHEEL_MIN_RADIUS = 0.2F;
    private static final float WHEEL_MAX_DENSITY = 100;

    private static final float WHEEL_MIN_DENSITY = 40F;

    public WheelDefinition wheelDefinition1 = new WheelDefinition();
    public WheelDefinition wheelDefinition2 = new WheelDefinition();
    public float chassisDensity;
    public List<Vec2> vertexList = new ArrayList<>(8);

    public CarDefinition() {

    }

    public static CarDefinition createRandomCar() {
        CarDefinition carDefinition = new CarDefinition();

        carDefinition.chassisDensity = Random.next(CHASSIS_MIN_DENSITY, CHASSIS_MAX_DENSITY);

        carDefinition.vertexList = new ArrayList<>(8);
        carDefinition.vertexList.add(new Vec2(Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS), 0F));
        carDefinition.vertexList.add(new Vec2(Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS), Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS)));
        carDefinition.vertexList.add(new Vec2(0F, Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS)));
        carDefinition.vertexList.add(new Vec2(-Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS), Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS)));
        carDefinition.vertexList.add(new Vec2(-Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS), 0F));
        carDefinition.vertexList.add(new Vec2(-Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS), -Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS)));
        carDefinition.vertexList.add(new Vec2(0F, -Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS)));
        carDefinition.vertexList.add(new Vec2(Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS), -Random.next(CHASSIS_MIN_AXIS, CHASSIS_MAX_AXIS)));

        List<Integer> left = Stream.of(0,1,2,3,4,5,6,7).collect(Collectors.toList());

        createWheelDefinition(carDefinition.wheelDefinition1, left);
        createWheelDefinition(carDefinition.wheelDefinition2, left);

        return carDefinition;
    }

    public static CarDefinition createMyBestCar() {
        CarDefinition carDefinition = new CarDefinition();

        carDefinition.chassisDensity = 200F;

        carDefinition.vertexList = new ArrayList<>(8);
        carDefinition.vertexList.add(new Vec2(1F, 0F));
        carDefinition.vertexList.add(new Vec2(1F, 0.1F));
        carDefinition.vertexList.add(new Vec2(0F, 0.1F));
        carDefinition.vertexList.add(new Vec2(-1F, 0.1F));
        carDefinition.vertexList.add(new Vec2(-1F, 0F));
        carDefinition.vertexList.add(new Vec2(-1F, -0.1F));
        carDefinition.vertexList.add(new Vec2(0F, -0.1F));
        carDefinition.vertexList.add(new Vec2(1F, -0.1F));

        carDefinition.wheelDefinition1.vertex = 0;
        carDefinition.wheelDefinition1.radius = 0.8F;
        carDefinition.wheelDefinition1.density = 60F;
        carDefinition.wheelDefinition2.vertex = 4;
        carDefinition.wheelDefinition2.radius = 0.8F;
        carDefinition.wheelDefinition2.density = 60F;

        return carDefinition;
    }

    private static void createWheelDefinition(WheelDefinition wheelDefinition, List<Integer> left) {
        int indexOfNext = Random.nextInt(left.size());
        wheelDefinition.vertex = left.remove(indexOfNext);
        wheelDefinition.radius = Random.next(WHEEL_MIN_RADIUS, WHEEL_MAX_RADIUS);
        wheelDefinition.density = Random.next(WHEEL_MIN_DENSITY, WHEEL_MAX_DENSITY);
    }

    @Override
    public String toString() {
        return "CarDefinition{" +
                "wheelDefinition1=" + wheelDefinition1 +
                ", wheelDefinition2=" + wheelDefinition2 +
                ", chassisDensity=" + chassisDensity +
                ", vertexList=" + vertexList +
                '}';
    }

    public static class WheelDefinition {

        public float radius;
        public float density;
        public int vertex;

        @Override
        public String toString() {
            return "WheelDefinition{" +
                    "radius=" + radius +
                    ", density=" + density +
                    ", vertex=" + vertex +
                    '}';
        }
    }
}
