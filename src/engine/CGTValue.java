package engine;

public abstract class CGTValue {

    /**
     * Adds two CGTValues into a new CGTValue object (as long as this is allowed)
     *
     * @param other The other CGTValue that shall be added
     * @return The sum of the two CGTValues
     * @throws IllegalAdditionException If this operation is not allowed
     */
    public abstract CGTValue add(CGTValue other) throws IllegalAdditionException;

    public abstract String toString();

    /**
     * @param leftValue  The result of choosing left's best option
     * @param rightValue The result of choosing right's best option
     * @return
     */
    //TODO: https://github.com/Fallscout/Konane/issues/2
    public static CGTValue getOutcome(CGTValue leftValue, CGTValue rightValue) {
        if (leftValue == null && rightValue == null) {
            //Berlekamp et al., p. 39
            return new Number(0);
        }

        if (leftValue == null && rightValue != null) {
            if (rightValue instanceof Number) {
                //Berlekamp et al., p. 44
                Number right = (Number) rightValue;
                return new Number(right.getValue() - 1);
            } else if (rightValue instanceof Nimber) {
                Nimber right = (Nimber) rightValue;
            } else if (rightValue instanceof Switch) {
                Switch right = (Switch) rightValue;
            } else if (rightValue instanceof Infinitesimal) {
                Infinitesimal right = (Infinitesimal) rightValue;
            }
        }

        if (leftValue != null && rightValue == null) {
            if (leftValue instanceof Number) {
                //Berlekamp et al., p. 44
                Number left = (Number) leftValue;
                return new Number(left.getValue() + 1);
            } else if (leftValue instanceof Nimber) {
                Nimber left = (Nimber) leftValue;
            } else if (leftValue instanceof Switch) {
                Switch left = (Switch) leftValue;
            } else if (leftValue instanceof Infinitesimal) {
                Infinitesimal left = (Infinitesimal) leftValue;
            }
        }

        if (leftValue instanceof Number) {
            Number left = (Number) leftValue;

            if (rightValue instanceof Number) {
                Number right = (Number) rightValue;
                // For fractions, see Berlekamp et al., p. 41
                // For switches, see Berlekamp et al., p. 141

                if (left.getValue() == 0 && right.getValue() == 0) {
                    return new Nimber(0);
                }

                if (left.getValue() + 1 == right.getValue()) {
                    // {n|n+1}=n+1/2 (Berlekamp et al., p. 27)
                    return new Number(left.getValue() + 0.5);
                }

                if (left.getValue() < 0 && right.getValue() > 0) {
                    return new Number(0);
                }

                if (left.getValue() > 0 && right.getValue() > 0) {
                    // find p and n
                    int p = 0;
                    int q = 0;
                    return new Number((2 * p + 1) / Math.pow(2, q + 1));
                }

                if (left.getValue() < 0 && right.getValue() < 0) {

                }
            } else if (rightValue instanceof Nimber) {
                Nimber right = (Nimber) rightValue;
            } else if (rightValue instanceof Switch) {
                Switch right = (Switch) rightValue;
            } else if (rightValue instanceof Infinitesimal) {
                Infinitesimal right = (Infinitesimal) rightValue;
            }

        } else if (leftValue instanceof Nimber) {
            Nimber left = (Nimber) leftValue;

            if (rightValue instanceof Number) {
                Number right = (Number) rightValue;
            } else if (rightValue instanceof Nimber) {
                Nimber right = (Nimber) rightValue;
            } else if (rightValue instanceof Switch) {
                Switch right = (Switch) rightValue;
            } else if (rightValue instanceof Infinitesimal) {
                Infinitesimal right = (Infinitesimal) rightValue;
            }

        } else if (leftValue instanceof Switch) {
            Switch left = (Switch) leftValue;

            if (rightValue instanceof Number) {
                Number right = (Number) rightValue;
            } else if (rightValue instanceof Nimber) {
                Nimber right = (Nimber) rightValue;
            } else if (rightValue instanceof Switch) {
                Switch right = (Switch) rightValue;
            } else if (rightValue instanceof Infinitesimal) {
                Infinitesimal right = (Infinitesimal) rightValue;
            }

        } else if (leftValue instanceof Infinitesimal) {
            Infinitesimal left = (Infinitesimal) leftValue;

            if (rightValue instanceof Number) {
                Number right = (Number) rightValue;
            } else if (rightValue instanceof Nimber) {
                Nimber right = (Nimber) rightValue;
            } else if (rightValue instanceof Switch) {
                Switch right = (Switch) rightValue;
            } else if (rightValue instanceof Infinitesimal) {
                Infinitesimal right = (Infinitesimal) rightValue;
            }
        }

        throw new IllegalArgumentException(
            "Not implemented for classes: left: " + (leftValue == null ? "null" : leftValue.getClass()) + ", right: " + (
                rightValue == null ? "null" : rightValue.getClass()) + ".");
    }

    public static CGTValue max(CGTValue firstValue, CGTValue secondValue) {
        if (firstValue == null) {
            return secondValue;
        }
        if (secondValue == null) {
            return firstValue;
        }

        if (firstValue instanceof Number) {
            if (secondValue instanceof Number) {
                //TODO:
            } else if (secondValue instanceof Nimber) {
                return firstValue;
            } else if (secondValue instanceof Switch) {
                return firstValue;
            } else if (secondValue instanceof Infinitesimal) {
                return firstValue;
            }
        } else if (firstValue instanceof Nimber) {
            if (secondValue instanceof Number) {
                return secondValue;
            } else if (secondValue instanceof Nimber) {
                //TODO:
            } else if (secondValue instanceof Switch) {
                return firstValue;
            } else if (secondValue instanceof Infinitesimal) {
                return firstValue;
            }
        } else if (firstValue instanceof Switch) {
            if (secondValue instanceof Number) {
                return secondValue;
            } else if (secondValue instanceof Nimber) {
                return secondValue;
            } else if (secondValue instanceof Switch) {
                //TODO:
            } else if (secondValue instanceof Infinitesimal) {
                return firstValue;
            }
        } else if (firstValue instanceof Infinitesimal) {
            if (secondValue instanceof Number) {
                return secondValue;
            } else if (secondValue instanceof Nimber) {
                return secondValue;
            } else if (secondValue instanceof Switch) {
                return secondValue;
            } else if (secondValue instanceof Infinitesimal) {
                //TODO:
            }
        }

        return null;
    }
}
