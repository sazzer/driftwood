# Skills

## Skill Checks

Performing a Skill Check requires 4 different numbers:

* S - Skill Level
* A - Attribute Level - Average if a skill depends on multiple Attributes
* T - Target Score
* C - Target Count

Out of these:

* `S` and `A` are dependant on the character
* `T` and `C` are dependant on the task being performed

A dice pool of size `A` is used, where each dice is of size `S`. 
Each dice has to equal `T` or higher, and a success is a counted if there are more than `C` successful rolls.

The formula for this is:
```
Ad (dS >= T) >= C
```
