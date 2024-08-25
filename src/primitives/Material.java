package primitives;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to represent a material
 */
@XmlRootElement
public class Material {
    /**
     * The diffuse coefficient
     */
    @XmlElement
    public Double3 kD = Double3.ZERO;

    /**
     * The specular coefficient
     */
    @XmlElement
    public Double3 kS = Double3.ZERO;

    /**
     * The shininess
     */
    @XmlElement
    public int nShininess = 0;

    /**
     * The transparency coefficient
     */
    @XmlElement
    public Double3 kT = Double3.ZERO;

    /**
     * The reflection coefficient
     */
    @XmlElement
    public Double3 kR = Double3.ZERO;

    /**
     * Glossiness factor
     */
    @XmlElement
    public double kG = 0.0;

    /**
     * Blurriness factor
     */
    @XmlElement
    public double kB = 0.0;


    /**
     * Set the diffuse coefficient
     *
     * @param kD the diffuse coefficient
     * @return the material (for chaining)
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Set the diffuse coefficient
     *
     * @param kD the diffuse coefficient
     * @return the material (for chaining)
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Set the specular coefficient
     *
     * @param kS the specular coefficient
     * @return the material (for chaining)
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Set the specular coefficient
     *
     * @param kS the specular coefficient
     * @return the material (for chaining)
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Set the shininess
     *
     * @param nShininess the shininess
     * @return the material (for chaining)
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /**
     * Set the transparency coefficient
     *
     * @param kT the transparency coefficient
     * @return the material (for chaining)
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Set the transparency coefficient
     *
     * @param kT the transparency coefficient
     * @return the material (for chaining)
     */
    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Set the reflection coefficient
     *
     * @param kR the reflection coefficient
     * @return the material (for chaining)
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Set the reflection coefficient
     *
     * @param kR the reflection coefficient
     * @return the material (for chaining)
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Set the glossiness factor
     *
     * @param kG the glossiness factor
     * @return the material (for chaining)
     */
    public Material setKg(double kG) {
        this.kG = kG;
        return this;
    }

    /**
     * Set the blurriness factor
     *
     * @param kB the blurriness factor
     * @return the material (for chaining)
     */
    public Material setKb(double kB) {
        this.kB = kB;
        return this;
    }
}
