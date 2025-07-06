# XY_Mental V1.1
This Android app, built in Java, is like a behavioral journal that shows mental skill cards to help support your daily life.

**Note:** I’m not a professional Android developer, so this project's code and structure might be a bit amateur.

## Features

- Create custom **mental skill badges** by providing:
  - Title  
  - Description  
  - Icon (60x60 dp)
- Pass the skills you want into the `SkillManager.java`, which handles their display and management within the app
- Save up to **9 user-selected skills** in the app by utilizing Android **SharedPreferences**
- Settings page allows users to customize:
  - Theme mode: light, dark, or system default  
  - Accent color: orange, blue, green, or purple  
  - Toggle audio, vibration, and animations on/off

## How to Add Skills

To add a new skill:

1. Define the skill title and description in `skills.xml` as shown in the example below:

```xml
<string name="skill_title">Your Skill Title Here</string>
<string name="skill_desc">Your skill description goes here, explaining the skill and how to apply it.</string>
```

2. Prepare the skill icon with dimensions **60x60 dp** and add it to the drawable resources.

3. Open `SkillManager.java` and add a new instance of the `Skill.java` class with the details for the new skill inside the singleton constructor where skills are managed.

Developers should refer to the `SkillManager.java` class for details on how skills are managed and added.
