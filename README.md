# GitBread

## Version control for baking (bread)

GitBread is a baking and recipe diary. It allows bakers to create or copy bread recipes 
to their recipe book, add notes, make modifications, and create new or improved recipes 
by recording variations in ingredients, cook times, cooking vessels and more. 

The GitBread concept of recipe version control is loosely based on [git](https://git-scm.com/),
a version control system for tracking changes in source code during software development.

GitBread is based on the concept of "Baker's Percentage", which expresses flour as 100% and all 
other ingredients as some percentage of the flour's weight. This method allows for quick and easy recipe scaling. 
You can read more about the this idea here: [King Arthur Flour: Bakers Percentage](https://www.kingarthurflour.com/pro/reference/bakers-percentage).

GitBread allows for things like:
- Recording recipes
- *branch* recipes to test variations
- *merge* changes back into your main recipe for when your changes improve the recipe
- *fork* the recipe in order to create an entirely new recipe based on a previous recipe
- Automatically record the weather for the day you made your bread
- Common bakers percentage calculations including support for preferments, poolish, hydration adjustments for milk and eggs, etc. 
- Record process notes like your kneading method, if you rested the dough, number and length of rises, autolyse, etc.
- Review your history of attempts and modifications for each recipe

 Anyone, beginner or expert, who wants to bake consistent loaves of bread or experiment and refine favorite recipes 
 will find the GitBread diary tool useful. Keep track of final recipes, the conditions that lead to the best version
 of an oft-baked bread, or simply go wild with experimentation knowing that you can easily look back at where you
 started and what you changed.
 

### Phase 1 User Stories

- As a user, I want to be able to add a recipe to my collection.
- As a user, I want to be able to view the titles of all my recipes.
- As a user, I want to be able to log an attempt of a recipe.
- As a user, I want to be able to view the number of times I have attempted a recipe.
- As a user, I want to be able scale a recipe up or down easily.
- As a user, I want to to be able to view the master version of a recipe.

### Phase 2 User Stories
- As a user, I want to to be asked if I want to save my collection when I quit the application.
- As a user, I want to be able to reload my collection to continue working on it. 
------
### Future User stories
- As a user, I want to be able to view the testing version of a recipe.
- As a user, I want to be able to make notes about the results of my attempt, and view these results later.
- As a user, I want to be able to test a variation of a recipe and replace the master if it is better, or leave
it in my recipe history if it makes it worse.
- As a user, I want to be able to set my location by typing in my address or city.


