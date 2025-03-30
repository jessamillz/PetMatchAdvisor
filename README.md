# PetMatch Advisor

PetMatch Advisor searches the PetFinder API for pets that fit your preferences. 
This is for anyone looking for a new fluffy (or not so fluffy) companion!

## API Reference

#### Get token

```https:
  GET //api.petfinder.com/v2/oauth2/token
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `client_id` | `string` | **Required**. Your API key |
| `client_secret` | `string` | **Required**. Your secret |

#### Get animal

```https:
  GET //api.petfinder.com/v2/animals?{parameter_1}={value_1}&{parameter_2}={value_2}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `type`      | `string` | **optional**. Type of animal to fetch |
| `good_with_children`      | `boolean` | **optional**. Returns results good with children |
| `location`      | `string` | **optional**. Return results by location |

More info here: https://www.petfinder.com/developers/v2/docs/

## Authors

- [@jessamillz](https://www.github.com/jessamillz)


## Properties

To run this project, you will need to add the following environment variables to your .properties file

`clientId`

`clientSecret`

