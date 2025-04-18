package gregtech.common.bees;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;

import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.EnumTolerance;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleBoolean;
import forestry.api.genetics.IAlleleInteger;
import forestry.api.genetics.IAlleleTolerance;
import forestry.api.genetics.IChromosomeType;
import forestry.api.lepidopterology.EnumButterflyChromosome;
import forestry.apiculture.flowers.FlowerProvider;
import forestry.core.config.Constants;
import forestry.core.genetics.alleles.AlleleBoolean;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.AlleleInteger;
import forestry.core.genetics.alleles.AlleleTolerance;
import forestry.core.genetics.alleles.EnumAllele;
import forestry.core.genetics.alleles.IAlleleValue;
import forestry.core.utils.vect.IVect;
import forestry.plugins.PluginManager;
import gregtech.GTMod;

public class GTAlleleHelper extends AlleleHelper {

    private static final String modId = Constants.ID;

    private Map<Class<?>, Map<?, ? extends IAllele>> alleleMaps = new HashMap<>();

    @Override
    public void init() {
        if (PluginManager.Module.APICULTURE.isEnabled()) {
            createAlleles(EnumAllele.Fertility.class, EnumBeeChromosome.FERTILITY);
            createAlleles(EnumAllele.Flowering.class, EnumBeeChromosome.FLOWERING);
        }

        if (PluginManager.Module.APICULTURE.isEnabled() || PluginManager.Module.ARBORICULTURE.isEnabled()) {
            createAlleles(EnumAllele.Territory.class, EnumBeeChromosome.TERRITORY, EnumTreeChromosome.TERRITORY);

            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.territoryDefault", get(EnumAllele.Territory.AVERAGE));
        }

        if (PluginManager.Module.APICULTURE.isEnabled() || PluginManager.Module.LEPIDOPTEROLOGY.isEnabled()) {
            createAlleles(EnumAllele.Speed.class, EnumBeeChromosome.SPEED, EnumButterflyChromosome.SPEED);
            createAlleles(EnumAllele.Lifespan.class, EnumBeeChromosome.LIFESPAN, EnumButterflyChromosome.LIFESPAN);
            createAlleles(
                EnumAllele.Tolerance.class,
                EnumBeeChromosome.TEMPERATURE_TOLERANCE,
                EnumBeeChromosome.HUMIDITY_TOLERANCE,
                EnumButterflyChromosome.TEMPERATURE_TOLERANCE,
                EnumButterflyChromosome.HUMIDITY_TOLERANCE);
            createAlleles(
                EnumAllele.Flowers.class,
                EnumBeeChromosome.FLOWER_PROVIDER,
                EnumButterflyChromosome.FLOWER_PROVIDER);

            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.speedNorm", get(EnumAllele.Speed.NORMAL));
        }

        if (PluginManager.Module.ARBORICULTURE.isEnabled()) {
            createAlleles(EnumAllele.Height.class, EnumTreeChromosome.HEIGHT);
            createAlleles(EnumAllele.Saplings.class, EnumTreeChromosome.FERTILITY);
            createAlleles(EnumAllele.Yield.class, EnumTreeChromosome.YIELD);
            createAlleles(EnumAllele.Fireproof.class, EnumTreeChromosome.FIREPROOF);
            createAlleles(EnumAllele.Maturation.class, EnumTreeChromosome.MATURATION);
            createAlleles(EnumAllele.Sappiness.class, EnumTreeChromosome.SAPPINESS);

            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.heightMax10", get(EnumAllele.Height.AVERAGE));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.saplingsDefault", get(EnumAllele.Saplings.AVERAGE));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.saplingsDouble", get(EnumAllele.Saplings.HIGH));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.saplingsTriple", get(EnumAllele.Saplings.HIGHER));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.yieldDefault", get(EnumAllele.Yield.AVERAGE));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.maturitySlowest", get(EnumAllele.Maturation.SLOWEST));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.maturitySlower", get(EnumAllele.Maturation.SLOWER));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.maturitySlow", get(EnumAllele.Maturation.SLOW));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.maturityAverage", get(EnumAllele.Maturation.AVERAGE));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.maturityFast", get(EnumAllele.Maturation.FAST));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.maturityFaster", get(EnumAllele.Maturation.FASTER));
            AlleleManager.alleleRegistry
                .registerDeprecatedAlleleReplacement("forestry.maturityFastest", get(EnumAllele.Maturation.FASTEST));
        }

        if (PluginManager.Module.LEPIDOPTEROLOGY.isEnabled()) {
            createAlleles(EnumAllele.Size.class, EnumButterflyChromosome.SIZE);
        }

        Map<Integer, IAlleleInteger> integers = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            IAlleleInteger alleleInteger = new AlleleInteger(modId, "i", i + "d", i, true);
            AlleleManager.alleleRegistry.registerAllele(
                alleleInteger,
                EnumTreeChromosome.GIRTH,
                EnumButterflyChromosome.METABOLISM,
                EnumButterflyChromosome.FERTILITY);
            integers.put(i, alleleInteger);
        }
        alleleMaps.put(Integer.class, integers);

        Map<Boolean, IAlleleBoolean> booleans = new HashMap<>();
        booleans.put(true, new AlleleBoolean(modId, "bool", true, false));
        booleans.put(false, new AlleleBoolean(modId, "bool", false, false));
        for (IAlleleBoolean alleleBoolean : booleans.values()) {
            AlleleManager.alleleRegistry.registerAllele(
                alleleBoolean,
                EnumBeeChromosome.NOCTURNAL,
                EnumBeeChromosome.TOLERANT_FLYER,
                EnumBeeChromosome.CAVE_DWELLING,
                EnumButterflyChromosome.NOCTURNAL,
                EnumButterflyChromosome.TOLERANT_FLYER,
                EnumButterflyChromosome.FIRE_RESIST);
        }
        alleleMaps.put(Boolean.class, booleans);
    }

    @SuppressWarnings("unchecked")
    public static void initialisation() {
        GTAlleleHelper helper = new GTAlleleHelper();

        try {
            helper.alleleMaps = (Map<Class<?>, Map<?, ? extends IAllele>>) FieldUtils
                .readField(FieldUtils.getField(AlleleHelper.class, "alleleMaps", true), AlleleHelper.instance, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        AlleleHelper.instance = helper;
        // AlleleHelper.instance.init();
    }

    @Override
    public <T extends Enum<T> & IChromosomeType> void set(IAllele[] alleles, T chromosomeType, IAllele allele) {

        if (allele == null) {
            GTMod.GT_FML_LOGGER.warn("Allele is null!");
            return;
        }

        if (!chromosomeType.getAlleleClass()
            .isInstance(allele)) {
            GTMod.GT_FML_LOGGER.warn("chromosomeType is not an instance of allele!" + allele.getName());
            return;
        }

        // TODO: uncomment this once all addon mods are using the allele registration with IChromosomeType
        // Collection<IChromosomeType> validTypes = AlleleManager.alleleRegistry.getChromosomeTypes(allele);
        // if (validTypes.size() > 0 && !validTypes.contains(chromosomeType)) {
        // throw new IllegalArgumentException("Allele can't be applied to this Chromosome type. Expected: " + validTypes
        // + " Got: " + chromosomeType);
        // }

        alleles[chromosomeType.ordinal()] = allele;
    }

    @Override
    public <T extends Enum<T> & IChromosomeType> void set(IAllele[] alleles, T chromosomeType, IAlleleValue value) {
        set(alleles, chromosomeType, get(value));
    }

    @Override
    public <T extends Enum<T> & IChromosomeType> void set(IAllele[] alleles, T chromosomeType, boolean value) {
        set(alleles, chromosomeType, get(value));
    }

    @Override
    public <T extends Enum<T> & IChromosomeType> void set(IAllele[] alleles, T chromosomeType, int value) {
        set(alleles, chromosomeType, get(value));
    }

    private IAllele get(Object value) {
        Class<?> valueClass = value.getClass();
        Map<?, ? extends IAllele> map = alleleMaps.get(valueClass);
        if (map == null) {
            throw new IllegalArgumentException("There is no IAllele type for: " + valueClass + ' ' + value);
        }
        IAllele allele = map.get(value);
        if (allele == null) {
            allele = new IAllele() {

                @Override
                public String getUID() {
                    return "NOT_FOUND";
                }

                @Override
                public boolean isDominant() {
                    return false;
                }

                @Override
                public String getName() {
                    return "NOT_FOUND";
                }

                @Override
                public String getUnlocalizedName() {
                    return "NOT_FOUND";
                }
            };
        }
        return allele;
    }

    private <K extends Enum<K> & IAlleleValue<V>, V> void createAlleles(Class<K> enumClass, IChromosomeType... types) {
        String category = enumClass.getSimpleName()
            .toLowerCase(Locale.ENGLISH);
        EnumMap<K, IAllele> map = new EnumMap<>(enumClass);
        for (K enumValue : enumClass.getEnumConstants()) {
            IAllele allele = createAllele(category, enumValue, types);
            map.put(enumValue, allele);
        }
        alleleMaps.put(enumClass, map);
    }

    private static <K extends IAlleleValue<V>, V> IAllele createAllele(String category, K enumValue,
        IChromosomeType... types) {
        V value = enumValue.getValue();
        boolean isDominant = enumValue.isDominant();
        String name = enumValue.toString()
            .toLowerCase(Locale.ENGLISH);

        Class<?> valueClass = value.getClass();
        if (Float.class.isAssignableFrom(valueClass)) {
            return AlleleManager.alleleFactory.createFloat(modId, category, name, (Float) value, isDominant, types);
        } else if (Integer.class.isAssignableFrom(valueClass)) {
            return AlleleManager.alleleFactory.createInteger(modId, category, name, (Integer) value, isDominant, types);
        } else if (IVect.class.isAssignableFrom(valueClass)) {
            IVect area = (IVect) value;
            return AlleleManager.alleleFactory
                .createArea(modId, category, name, area.getX(), area.getY(), area.getZ(), isDominant, types);
        } else if (Boolean.class.isAssignableFrom(valueClass)) {
            return AlleleManager.alleleFactory.createBoolean(modId, category, (Boolean) value, isDominant, types);
        } else if (EnumTolerance.class.isAssignableFrom(valueClass)) {
            IAlleleTolerance alleleTolerance = new AlleleTolerance(
                modId,
                category,
                name,
                (EnumTolerance) value,
                isDominant);
            AlleleManager.alleleRegistry.registerAllele(alleleTolerance, types);
            return alleleTolerance;
        } else if (FlowerProvider.class.isAssignableFrom(valueClass)) {
            return AlleleManager.alleleFactory
                .createFlowers(modId, category, name, (FlowerProvider) value, isDominant, types);
        }
        throw new RuntimeException("could not create allele for category: " + category + " and value " + valueClass);
    }
}
