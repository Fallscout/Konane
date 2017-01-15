package engine;

public abstract class CGTValue {

    public abstract String toString();

    public abstract boolean equals(Object object);

    // TODO: https://github.com/Fallscout/Konane/issues/2
    public static CGTValue getOutcome(CGTValue leftValue, CGTValue rightValue) {
        if (leftValue == null && rightValue == null) {
            // Berlekamp et al., p. 39
            return new Number(0);
        }

        if (leftValue == null && rightValue != null) {
            if (rightValue instanceof Number) {
                // Berlekamp et al., p. 44
                Number right = (Number) rightValue;
                return new Number(right.getValue() - 1);
            } else if (rightValue instanceof Nimber) {
                // Should not be possible
                throw new IllegalStateException("It should not be possible that left has no moves but right got star.");
            } else if (rightValue instanceof Switch) {
                // Should not be possible
                throw new IllegalStateException("It should not be possible that left has no moves but right got switch.");
            } else if (rightValue instanceof Infinitesimal) {
                // Should not be possible
                throw new IllegalStateException("It should not be possible that left has no moves but right got infinitesimal.");
            }
        }

        if (leftValue != null && rightValue == null) {
            if (leftValue instanceof Number) {
                // Berlekamp et al., p. 44
                Number left = (Number) leftValue;
                return new Number(left.getValue() + 1);
            } else if (leftValue instanceof Nimber) {
                // Should not be possible
                throw new IllegalStateException("It should not be possible that right has no moves but left got star.");
            } else if (leftValue instanceof Switch) {
                // Should not be possible
                throw new IllegalStateException("It should not be possible that right has no moves but left got switch.");
            } else if (leftValue instanceof Infinitesimal) {
                // Should not be possible
                throw new IllegalStateException("It should not be possible that right has no moves but left got infinitesimal.");
            }
        }

        if (leftValue instanceof Number) {
            Number left = (Number) leftValue;

            if (rightValue instanceof Number) {
                Number right = (Number) rightValue;
                // For fractions, see Berlekamp et al., p. 41
                // For switches, see Berlekamp et al., p. 141

                if (left.getValue() == 0 && right.getValue() == 0) {
                    return new Nimber(1);
                }

                if (left.getValue() == right.getValue()) {
                    return new Number(left.getValue());
                }

                if (left.getValue() > right.getValue()) {
                    return new Switch(left, right);
                }

                if (left.getValue() + 1 == right.getValue()) {
                    // {n|n+1}=n+1/2 (Berlekamp et al., p. 27)
                    return new Number(left.getValue() + 0.5);
                }

                if (left.getValue() < 0 && right.getValue() > 0) {
                    return new Number(0);
                }

                if (left.getValue() > 0 && right.getValue() > 0) {
                    return new Number(left.getValue() + 1);
                }

                if (left.getValue() < 0 && right.getValue() < 0) {
                    return new Number(right.getValue() - 1);
                }

                //TODO: Discuss with Jos
                if (left.getValue() < 0 && right.getValue() == 0) {
                    return new Number(left.getValue());
                }

                //TODO: Discuss with Jos
                if (left.getValue() == 0 && right.getValue() > 0) {
                    return new Number(right.getValue());
                }
            } else if (rightValue instanceof Nimber) {
                if (left.getValue() > 0) {
                    return new Number(left.getValue());
                } else if (left.getValue() == 0) {
                    return new Infinitesimal(1);
                } else {
                    return new Number(0);
                }
            } else if (rightValue instanceof Switch) {
                Switch right = (Switch) rightValue;
                if (right.getLeft().getValue() > 0 && right.getRight().getValue() > 0) {
                    if (left.getValue() > 0) {
                        return new Number(left.getValue());
                    } else if (left.getValue() == 0) {
                        return new Infinitesimal(1);
                    } else {
                        return new Number(0);
                    }
                } else if (right.getLeft().getValue() < 0 && right.getRight().getValue() < 0) {
                    if (left.getValue() > 0) {
                        return new Nimber(1);
                    } else if (left.getValue() == 0) {
                        return new Nimber(1);
                    } else {
                        return new Number(left.getValue());
                    }
                } else {
                    if (left.getValue() > 0) {
                        return new Number(left.getValue());
                    } else if (left.getValue() == 0) {
                        return new Infinitesimal(1);
                    } else {
                        return new Number(0);
                    }
                }
            } else if (rightValue instanceof Infinitesimal) {
                Infinitesimal right = (Infinitesimal) rightValue;
                if (right.getValue() > 0) {
                    if (left.getValue() > 0) {
                        return new Number(left.getValue());
                    } else if (left.getValue() == 0) {
                        return new Infinitesimal(1);
                    } else {
                        return new Number(0);
                    }
                } else {
                    if (left.getValue() > 0) {
                        return new Nimber(1);
                    } else if (left.getValue() == 0) {
                        return new Nimber(1);
                    } else {
                        return new Number(left.getValue());
                    }
                }
            }

        } else if (leftValue instanceof Nimber) {
            Nimber left = (Nimber) leftValue;

            if (rightValue instanceof Number) {
                Number right = (Number) rightValue;
                if (right.getValue() > 0) {
                    return new Nimber(1);
                } else if (right.getValue() == 0) {
                    return new Infinitesimal(-1);
                } else {
                    return new Number(right.getValue());
                }
            } else if (rightValue instanceof Nimber) {
                Nimber right = (Nimber) rightValue;
                int result = left.getValue() ^ right.getValue();
                if(result == 0) {
                	return new Number(0);
                } else {
                	return new Nimber(result);
                }
            }

        } else if (leftValue instanceof Switch) {
            Switch left = (Switch) leftValue;

            if (rightValue instanceof Number) {
                Number right = (Number) rightValue;
                if (left.getLeft().getValue() > 0 && left.getRight().getValue() > 0) {
                    if (right.getValue() > 0) {
                        return new Number(right.getValue());
                    } else if (right.getValue() == 0) {
                        return new Nimber(1);
                    } else {
                        return new Nimber(1);
                    }
                } else if (left.getLeft().getValue() < 0 && left.getRight().getValue() < 0) {
                    if (right.getValue() > 0) {
                        return new Number(0);
                    } else if (right.getValue() == 0) {
                        return new Infinitesimal(-1);
                    } else {
                        return new Number(right.getValue());
                    }
                } else {
                    if (right.getValue() > 0) {
                        return new Number(0);
                    } else if (right.getValue() == 0) {
                        return new Infinitesimal(-1);
                    } else {
                        return new Number(right.getValue());
                    }
                }
            }

        } else if (leftValue instanceof Infinitesimal) {
            Infinitesimal left = (Infinitesimal) leftValue;

            if (rightValue instanceof Number) {
                Number right = (Number) rightValue;
                if (left.getValue() > 0) {
                    if (right.getValue() > 0) {
                        return new Number(right.getValue());
                    } else if (right.getValue() == 0) {
                        return new Nimber(1);
                    } else {
                        return new Nimber(1);
                    }
                } else {
                    if (right.getValue() > 0) {
                        return new Number(0);
                    } else if (right.getValue() == 0) {
                        return new Infinitesimal(-1);
                    } else {
                        return new Number(right.getValue());
                    }
                }
            }
        }

        // If value cannot be simplified, return nothing and carry on with
        // Alpha-Beta-Search
        return null;
    }

    public static CGTValue max(CGTValue firstValue, CGTValue secondValue, boolean blackTurn) {

        if (firstValue == null) {
            return secondValue;
        }

        if (secondValue == null) {
            return firstValue;
        }

        if (firstValue instanceof Number) {
            if (secondValue instanceof Number) {

                Number first = (Number) firstValue;
                Number second = (Number) secondValue;

                if (blackTurn) {
                    if (first.getValue() > second.getValue()) {
                        return first;
                    } else {
                        return second;
                    }
                } else {
                    if (first.getValue() < second.getValue()) {
                        return first;
                    } else {
                        return second;
                    }
                }

            } else if (secondValue instanceof Nimber) {

                Number first = (Number) firstValue;
                Nimber second = (Nimber) secondValue;

                if (blackTurn) {

                } else {

                }

            } else if (secondValue instanceof Switch) {
                return firstValue;
            } else if (secondValue instanceof Infinitesimal) {
                return firstValue;
            }
        } else if (firstValue instanceof Nimber) {
            if (secondValue instanceof Number) {
                return secondValue;
            } else if (secondValue instanceof Nimber) {
                // TODO:
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
                // TODO:
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
                // TODO:
            }
        }

        throw new IllegalArgumentException("Cannot compare values.");
    }

    public static boolean lessEqual(CGTValue first, CGTValue second) {
        CGTValue outcomeBlack = CGTValue.max(first, second, true);
        CGTValue outcomeWhite = CGTValue.max(first, second, false);
        return (outcomeBlack == second && outcomeWhite == first) || first.equals(second);
    }

    public static boolean less(CGTValue first, CGTValue second) {
        CGTValue outcomeBlack = CGTValue.max(first, second, true);
        CGTValue outcomeWhite = CGTValue.max(first, second, false);
        return outcomeBlack == second && outcomeWhite == first && !first.equals(second);
    }

    public static boolean greaterEqual(CGTValue first, CGTValue second) {
        CGTValue outcomeBlack = CGTValue.max(first, second, true);
        CGTValue outcomeWhite = CGTValue.max(first, second, false);
        return (outcomeBlack == first && outcomeWhite == second) || first.equals(second);
    }

    public static boolean greater(CGTValue first, CGTValue second) {
        CGTValue outcomeBlack = CGTValue.max(first, second, true);
        CGTValue outcomeWhite = CGTValue.max(first, second, false);
        return outcomeBlack == first && outcomeWhite == second && !first.equals(second);
    }

    // public boolean lessEquals(CGTValue other) {
    // if (other == null) {
    // throw new IllegalArgumentException("'other' cannot be null.");
    // } else {
    // if (this instanceof Number) {
    // if (other instanceof Number) {
    // if (((Number) this).getValue() <= ((Number) other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else if (other instanceof Nimber) {
    // return false;
    // } else if (other instanceof Switch) {
    // return false;
    // } else if (other instanceof Infinitesimal) {
    // return false;
    // }
    // } else if (this instanceof Nimber) {
    // if (other instanceof Number) {
    // return true;
    // } else if (other instanceof Nimber) {
    // if (((Nimber) this).getValue() <= ((Nimber) other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else if (other instanceof Switch) {
    // return false;
    // } else if (other instanceof Infinitesimal) {
    // return false;
    // }
    // } else if (this instanceof Switch) {
    // if (other instanceof Number) {
    // return true;
    // } else if (other instanceof Nimber) {
    // return true;
    // } else if (other instanceof Switch) {
    // throw new IllegalArgumentException("Cannot compare switch and switch.");
    // } else if (other instanceof Infinitesimal) {
    // return false;
    // }
    // } else if (this instanceof Infinitesimal) {
    // if (other instanceof Number) {
    // return true;
    // } else if (other instanceof Nimber) {
    // return true;
    // } else if (other instanceof Switch) {
    // return true;
    // } else if (other instanceof Infinitesimal) {
    // if (((Infinitesimal) this).getValue() <= ((Infinitesimal)
    // other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // }
    // }
    // }
    //
    // throw new IllegalArgumentException("Cannot compare values.");
    // }
    //
    // public boolean greaterEqual(CGTValue other) {
    // if (other == null) {
    // throw new IllegalArgumentException("'other' cannot be null.");
    // } else {
    // if (this instanceof Number) {
    // if (other instanceof Number) {
    // if (((Number) this).getValue() >= ((Number) other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else if (other instanceof Nimber) {
    // return true;
    // } else if (other instanceof Switch) {
    // return true;
    // } else if (other instanceof Infinitesimal) {
    // return true;
    // }
    // } else if (this instanceof Nimber) {
    // if (other instanceof Number) {
    // return false;
    // } else if (other instanceof Nimber) {
    // if (((Nimber) this).getValue() >= ((Nimber) other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else if (other instanceof Switch) {
    // return true;
    // } else if (other instanceof Infinitesimal) {
    // return true;
    // }
    // } else if (this instanceof Switch) {
    // if (other instanceof Number) {
    // return false;
    // } else if (other instanceof Nimber) {
    // return false;
    // } else if (other instanceof Switch) {
    //
    // } else if (other instanceof Infinitesimal) {
    // return true;
    // }
    // } else if (this instanceof Infinitesimal) {
    // if (other instanceof Number) {
    // return false;
    // } else if (other instanceof Nimber) {
    // return false;
    // } else if (other instanceof Switch) {
    // return false;
    // } else if (other instanceof Infinitesimal) {
    // if (((Infinitesimal) this).getValue() >= ((Infinitesimal)
    // other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // }
    // }
    // }
    //
    // throw new IllegalArgumentException("Cannot compare values.");
    // }
    //
    // public boolean less(CGTValue other) {
    // if (other == null) {
    //
    // } else {
    // if (this instanceof Number && other instanceof Number) {
    // if (((Number) this).getValue() < ((Number) other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else if (this instanceof Nimber && other instanceof Nimber) {
    // if (((Nimber) this).getValue() < ((Nimber) other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else if (this instanceof Switch && other instanceof Switch) {
    //
    // } else if (this instanceof Infinitesimal && other instanceof
    // Infinitesimal) {
    // if (((Infinitesimal) this).getValue() < ((Infinitesimal)
    // other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else {
    // return this.lessEquals(other);
    // }
    // }
    //
    // throw new IllegalArgumentException("Cannot compare values.");
    // }
    //
    // public boolean greater(CGTValue other) {
    // if (other == null) {
    // throw new IllegalArgumentException("'other' cannot be null.");
    // } else {
    // if (this instanceof Number && other instanceof Number) {
    // if (((Number) this).getValue() > ((Number) other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else if (this instanceof Nimber && other instanceof Nimber) {
    // if (((Nimber) this).getValue() > ((Nimber) other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else if (this instanceof Switch && other instanceof Switch) {
    //
    // } else if (this instanceof Infinitesimal && other instanceof
    // Infinitesimal) {
    // if (((Infinitesimal) this).getValue() > ((Infinitesimal)
    // other).getValue()) {
    // return true;
    // } else {
    // return false;
    // }
    // } else {
    // return this.greaterEqual(other);
    // }
    // }
    //
    // throw new IllegalArgumentException("Cannot compare values.");
    // }
}
